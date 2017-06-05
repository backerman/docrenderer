(ns org.facefault.docrenderer.handler
  (:import java.io.File)
  (:require [ring.util.response :as response]
            [org.facefault.docrenderer.render :as render]
            [org.facefault.docrenderer.transform :as transform]
            [me.raynes.conch :refer [with-programs]]
            [me.raynes.fs :as fs]
            [clojure.java.io :as io]
            [clojure.tools.logging :as log]
            [ring.util.io :as ring-io]
            [ring.middleware.defaults :refer :all]
            [ring.middleware.params :as params]))

(defn render-handler [my-stylesheet columns rows]
  "Return a Ring handler for rendering the provided stylesheet, which will
   be n-upped with the provided number of columns/rows onto US Letter pages
   in landscape orientation.

   This handler should, at a minimum, be wrapped by wrap-multipart-params."
  (fn [request]
    (let [rendered-fn (File/createTempFile "rendered" ".pdf")
          nup-fn  (File/createTempFile "nup" ".pdf")]
      (with-open [in-stream (-> request
                               :params
                               :data
                               :tempfile
                               io/input-stream)]
        (with-open [rendered (io/output-stream rendered-fn)]
          (render/transform-and-render my-stylesheet in-stream rendered))
        (with-programs [pdfnup]
          (let [out-writer (java.io.StringWriter.)
                err-writer (java.io.StringWriter.)]
            (try
              (pdfnup "--nup" (format "%dx%d" columns rows)
                      "--paper" "usletter"
                      "--frame" "true"
                      "--landscape"
                      "--outfile" (.getAbsolutePath nup-fn)
                      (.getAbsolutePath rendered-fn)
                      {:timeout 20000
                       :out out-writer
                       :err err-writer
                       :verbose true})
              (-> (response/response
                  (ring-io/piped-input-stream
                   (fn [out-stream]
                     (io/copy nup-fn out-stream)
                     (.flush out-stream)
                     (.delete rendered-fn)
                     (.delete nup-fn))))
                 (response/content-type "application/pdf"))
              (catch Exception e
                (.flush out-writer)
                (.flush err-writer)
                (log/error "Unable to complete imposition. Exception:\n"
                           (ex-data e)
                           "\nSTDOUT:\n"
                           (.toString out-writer)
                           "\nSTDERR:\n"
                           (.toString err-writer))
                {:status 500
                 :body "<h1></h1>"
                 }))))))))

(defn wrap-multipart
  [handler]
  (wrap-defaults handler
                 (assoc-in api-defaults [:params :multipart] true)))

(defn wrap-exceptions
  "Catch all exceptions thrown by the handler and return an error page."
  [handler]
  (fn [request]
    (try
      (handler request)
      (catch Exception e
        (log/error "Exception thrown when processing request:" e)
        (-> "html/error.html"
           io/resource
           slurp ; response/file-response would want a File, not a URL.
           response/response
           (response/content-type "text/html")
           (response/status 503))))))
