## Useful stuff

### lein repl

In a `lein repl`, to load everything/refresh:

``` clojure
(require '[clojure.tools.namespace.repl :refer [refresh]])
(refresh)
```

### XPath in the REPL

Loading a document into the REPL and running XPath queries against it:

``` clojure
(def proc (net.sf.saxon.s9api.Processor. false))
(def streamsource
  (javax.xml.transform.stream.StreamSource.
   "/home/bsa3/src/balticon-xsl/bidsheet.xsl"))
(def stylesheet
  (-> proc .newDocumentBuilder (.build streamsource)))
(def xpCompiler
  (doto (.newXPathCompiler proc)
    (.declareNamespace "xsl" "http://www.w3.org/1999/XSL/Transform")
    (.declareNamespace "fo" "http://www.w3.org/1999/XSL/Format")))
(def ctxnode
  (let
      [compiled-query
       (-> xpCompiler
           (.compile "//fo:block[@select=\"artwork/name\"]") .load)]
    (.setContextItem compiled-query stylesheet)
    (.evaluateSingle compiled-query)))
```
