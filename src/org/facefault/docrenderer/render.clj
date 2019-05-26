(ns org.facefault.docrenderer.render
  (:import (org.apache.fop.apps Fop
                                FopFactory
                                MimeConstants)
           org.apache.fop.render.xml.XMLRenderer
           (java.io ByteArrayOutputStream
                    OutputStreamWriter
                    FileOutputStream
                    File)
           javax.xml.transform.sax.SAXTransformerFactory
           javax.xml.transform.stream.StreamResult)
  (:require [clojure.java.io :as io]
            [clojure.tools.logging :as log]
            (org.facefault.docrenderer [transform :as xform])))

(defn- copy-resource-to-file
  "Copies a classpath resource to a file, returning a java.io.File.
  The caller should delete the file when finished with it."
  [resource-name]
  (let [tempfile (File/createTempFile "fopconfig" ".xml")]
    (io/copy (-> resource-name io/resource slurp) tempfile)
    tempfile))

(defn- new-factory
  "Create a new FOP factory and give it our configuration file."
  []
  (let [tempfile (copy-resource-to-file "config/fopconfig.xml")
        factory  (FopFactory/newInstance tempfile)]
    (.delete tempfile)
    factory))

(def my-factory (new-factory))

(def sax-transformer-factory (SAXTransformerFactory/newInstance))

(defn- new-fop
  "Create a FOP from the factory. If a MIME type is not provided,
  default to PDF."
  ([factory output-stream]
   (new-fop factory output-stream MimeConstants/MIME_PDF))
  ([factory output-stream mime-type]
   (.newFop factory mime-type output-stream)))

(defn- xml-rendering-fop
  ([factory output-stream]
   (xml-rendering-fop factory output-stream MimeConstants/MIME_PDF))
  ([factory output-stream mime-type]
   (let [user-agent (.newFOUserAgent factory)
         renderer (XMLRenderer. user-agent)
         handler (.newTransformerHandler sax-transformer-factory)
         result (StreamResult. output-stream)]
     (.mimicRenderer renderer
                     (-> user-agent
                         .getRendererFactory
                         (.createRenderer user-agent mime-type)))
     (.setResult handler result)
     (.setRendererOverride user-agent renderer)
     (.setContentHandler renderer handler)
     (.newFop factory MimeConstants/MIME_FOP_AREA_TREE user-agent))))

(defn transform-and-render
  "Transform the input XML (in-stream) with the provided (compiled)
  stylesheet."
  [in-stylesheet in-stream out-stream]
  (let [fop (new-fop my-factory out-stream)
        xsl-out (xform/sax-destination (.getDefaultHandler fop))]
    (xform/transform in-stylesheet in-stream xsl-out)))
