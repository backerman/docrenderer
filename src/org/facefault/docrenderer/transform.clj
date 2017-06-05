(ns org.facefault.docrenderer.transform
  (:import (net.sf.saxon.s9api Processor
                               SAXDestination)
           net.sf.saxon.stax.XMLStreamWriterDestination
           javax.xml.stream.XMLOutputFactory
           javax.xml.transform.stream.StreamSource
           javax.xml.transform.sax.SAXSource
           java.io.File
           org.xml.sax.InputSource
           org.xml.sax.helpers.XMLReaderFactory)
  (:require [clojure.java.io :as io]
            [clojure.tools.logging :as log]))

(defn compile-stylesheet
  "Compiles an XSL stylesheet from a provided InputStream."
  [xsl]
  (try
    (-> (Processor. false)
       .newXsltCompiler
      (.compile (StreamSource. xsl)))
    (catch Exception e
      (log/error "Couldn't compile stylesheet!" e))))

(defn compile-stylesheet-file
  "Compiles an XSL stylesheet from a file on disk."
  [filename]
  (with-open [r (io/input-stream filename)]
    (compile-stylesheet r)))

(defn xml-destination
  "Create a Saxon s9api destination from an output stream."
  [outstream]
  (-> (XMLOutputFactory/newFactory)
     (.createXMLStreamWriter outstream)
     XMLStreamWriterDestination.))

(defn sax-destination
  "Create a Saxon s9api destination from a SAX ContentHandler."
  [content-handler]
  (SAXDestination. content-handler))

(defn- xml-reader
  "Create an XMLReader instance suitable for parsing untrusted XML."
  []
  ;; This function is adapted from the instructions at
  ;; https://www.owasp.org/index.php/XML_External_Entity_(XXE)_Prevention_Cheat_Sheet#Java
  (doto (XMLReaderFactory/createXMLReader)
    (.setFeature
     "http://xml.org/sax/features/external-general-entities"
     false)
    (.setFeature
     "http://xml.org/sax/features/external-parameter-entities"
     false)
    (.setFeature
     "http://apache.org/xml/features/nonvalidating/load-external-dtd"
     false)))

(defn- untrusted-xml-source
  "Create a Source for reading untrusted XML from an InputStream."
  [in-stream]
  (SAXSource. (xml-reader) (InputSource. in-stream)))

(defn transform
  "Process the input XML data through the compiled stylesheet and write to
   the provided s9api destination."
  [stylesheet inxml destination]
  (doto (.load stylesheet)
    (.setSource (untrusted-xml-source inxml))
    (.setDestination destination)
    (.transform)))
