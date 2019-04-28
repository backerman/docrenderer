;; There are side effects in top-level code in some of the dependencies.
;; To get around this for now, we have this stub main that calls the actual
;; main.

(ns org.facefault.docrenderer.main
  (:gen-class))

(defn -main
  []
  (require 'org.facefault.docrenderer.core)
  ((resolve 'org.facefault.docrenderer.core/start)))
