(ns org.facefault.docrenderer.render
  (:import (org.apache.fop.apps Fop
                                FopFactory
                                MimeConstants)
           (java.io OutputStreamWriter
                    FileOutputStream
                    File)
           javax.xml.transform.stream.StreamSource)
  (:require [clojure.java.io :as io]
            [clojure.tools.logging :as log]
            [org.facefault.docrenderer.transform :as xform]))

(defn- copy-resource-to-file
  "Copies a classpath resource to a file, returning a java.io.File.
  The caller should delete the file when finished with it."
  [resource-name]
  (let [tempfile (File/createTempFile "fopconfig" "xml")]
    (io/copy (-> resource-name io/resource slurp) tempfile)
    tempfile))

(defn- new-factory
  "Create a new FOP factory and give it our configuration file."
  []
  (let [tempfile (copy-resource-to-file "config/fopconfig.xml")
        factory  (FopFactory/newInstance tempfile)]
    (.delete tempfile)
    factory))

(defn- new-fop
  "Create a FOP from the factory and set to PDF output."
  [factory output-stream]
  (.newFop factory MimeConstants/MIME_PDF output-stream))

(defn transform-and-render
  "Transform the input XML (in-stream) with the provided (compiled)
  stylesheet."
  [in-stylesheet in-stream out-stream]
  (let [fop (new-fop (new-factory) out-stream)
        xsl-out (xform/sax-destination (.getDefaultHandler fop))]
    (xform/transform in-stylesheet in-stream xsl-out)))
