(ns org.facefault.docrenderer.config
  (:gen-class)
  (:import java.io.FileNotFoundException)
  (:require [clj-toml.core :as toml]
            [environ.core :refer [env]]
            [clojure.java.io :as io]
            [clojure.tools.logging :as log]
            [compojure.core :refer [POST]]
            [me.raynes.fs :as fs]
            [org.facefault.docrenderer.handler :as handler]
            [org.facefault.docrenderer.transform :as transform]))

(def ^:private stylesheets-dir
  (env :stylesheets-dir))

(def ^:private config-file
  (env :config-file))

(defn- get-config []
  (with-open [f (io/reader config-file)]
    (toml/parse-string (slurp f))))

(defn- generate-route [config routename]
  (fs/with-cwd stylesheets-dir
    (let [my-config (config routename)
          nup (my-config "nup")
          stylesheet-filename (my-config "stylesheet")]
      (if (nil? stylesheet-filename)
        (throw (FileNotFoundException.
                (str "Route '" routename "' has missing or invalid"
                     " stylesheet parameter"))))
      (let [xsl (transform/compile-stylesheet-file
                 (fs/file stylesheet-filename))
            args (if nup
                   (list xsl (nup "across") (nup "down"))
                   (list xsl 1 1))]
        (POST
         (str "/" routename)
         []
         (apply handler/render-handler args))))))

(defn routes []
  "Return this application's routes as specified in the configuration file."
  (fs/with-cwd stylesheets-dir
    (let [config (get-config)]
      (map (fn [x] (generate-route config (first x))) config))))
