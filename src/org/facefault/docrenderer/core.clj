(ns org.facefault.docrenderer.core
  (:gen-class)
  (:import java.io.File)
  (:require [ring.adapter.jetty :as jetty]
            [ring.util.response :as response]
            [org.facefault.docrenderer.render :as render]
            [org.facefault.docrenderer.transform :as transform]
            [org.facefault.docrenderer.config :as config]
            [org.facefault.docrenderer.handler :refer :all]
            [me.raynes.conch :refer [with-programs]]
            [me.raynes.fs :as fs]
            [clojure.java.io :as io]
            [clojure.tools.logging :as log]
            [ring.util.io :as ring-io]
            [ring.middleware.defaults :refer :all]
            [ring.middleware.params :as params]
            [ring.logger :as logger]
            [compojure.core :refer [routes]]
            [environ.core :refer [env]]
            [compojure.route :as route]))

(defn rendering-routes []
  (apply routes
         (concat
          (config/routes)
          (list (route/not-found "<h1>Error!</h1>")))))

(defn -main []
  (try
    (jetty/run-jetty (-> (rendering-routes)
                         wrap-exceptions
                         wrap-multipart
                         logger/wrap-with-logger)
                     {:port 3000})
    (catch Exception e
      (log/fatal "Unable to start application:" e))))
