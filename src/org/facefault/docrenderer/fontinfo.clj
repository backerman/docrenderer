(ns org.facefault.docrenderer.fontinfo
  (:import (org.apache.fop.fonts DefaultFontConfig
                                 DefaultFontConfig$DefaultFontConfigParser
                                 DefaultFontConfigurator
                                 Font
                                 FontCollection
                                 FontInfo
                                 FontSetup)
           org.apache.fop.tools.fontlist.FontListGenerator
           org.apache.fop.apps.io.ResourceResolverFactory
           org.apache.xmlgraphics.util.MimeConstants)
  (:require [org.facefault.docrenderer.render :as render]))

;; Lovingly ripped off from
;; http://apache-fop.1065347.n5.nabble.com/Extract-font-width-using-FOP-td37360.html
(defn font-info
  "Create a FontInfo object with our FOP configuration and register all fonts."
  []
  (let [factory render/my-factory
        user-agent (.newFOUserAgent factory)
        document-handler (-> factory
                             .getRendererFactory
                             (.createDocumentHandler user-agent
                                                     MimeConstants/MIME_PDF))
        configurator (.getConfigurator document-handler)
        fi (FontInfo.)]
    (.setupFontInfo configurator (.getMimeType document-handler) fi)
    (.setFontInfo document-handler fi)
    fi))

(defn string-width
  "Get the width (approximate) of a `text` string in points."
  [text font-info font-triplet font-size]
  ; Is font size decipoints?
  (let [font (.getFontInstance font-info font-triplet font-size)
        metrics (.getFontMetrics font)]
    (/ (reduce (fn [acc c] (+ acc (.getWidth metrics
                                            (int (.mapChar font c))
                                            font-size)))
               ;; FIXME does this work for non-BMP characters?
               0 (.toCharArray text)) 1000)))

(defn- display-fonts
  ;; Handy debugging routine.
  []
  (let [info (font-info)]
    (run! (fn [tf-entry]
            (let [typeface (.getValue tf-entry)]
              (printf "Font: %s\nFamilies: %s\n\n"
                      (.getFullName typeface)
                      (.getFamilyNames typeface)
                      )))
          (.getFonts info))))
