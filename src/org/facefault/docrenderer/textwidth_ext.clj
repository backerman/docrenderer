(ns org.facefault.docrenderer.textwidth-ext
  (:import (net.sf.saxon.lib ExtensionFunctionCall
                             ExtensionFunctionDefinition)
           (net.sf.saxon.om  StructuredQName)
           (net.sf.saxon.tree.util Navigator)
           (net.sf.saxon.value SequenceType
                               StringValue)
           (net.sf.saxon.expr StaticContext)
           (net.sf.saxon.style ExpressionContext
                               XSLTStaticContext))
  (:require [clojure.tools.logging :as log]
            [clojure.string :as string]))

(def our-uri "https://github.com/backerman/docrenderer")

(def ^:private static-xpaths
  "XPath expressions that aren't automatically generated below."
  {:text "normalize-space(string-join(text()))"})

(def xpaths
  "Map of our XPath queries that we use to get our parameters."
  (reduce (fn [acc selector]
            (assoc acc
                   (keyword selector)
                   (string/replace "data(ancestor-or-self::*/attribute(SEL))"
                                   "SEL" selector)))
          static-xpaths
          ["font-size" "font-weight" "font-family"]))

(defn compiled-xpaths
  "XPath queries compiled for our `Processor`."
  [processor]
  (let [compiler (.newXPathCompiler processor)]
    (doseq [[prefix namespc] [["xsl" "http://www.w3.org/1999/XSL/Transform"]
                              ["fo" "http://www.w3.org/1999/XSL/Format"]
                              ["xs" "http://www.w3.org/2001/XMLSchema"]]]
      (.declareNamespace compiler prefix namespc))
    (into {} (for [[sel expr] xpaths] [sel (.compile compiler expr)]))))

(defn get-params
  "Get the font parameters necessary to calculate the text size.
  Returns a map with :font-family, :font-size, :font-weight.
  "
  [my-node xpath-queries]
  (into {} (for [[sel query] xpath-queries]
             [sel (some->
                   (doto (.load query)
                     (.setContextItem my-node))
                   .evaluateSingle
                   .toString)])))

(defn get-extension
  "Return a new instance of our extension."
  [processor]
  (proxy [net.sf.saxon.lib.ExtensionFunctionDefinition]
      []
    (hasSideEffects [] true)
    (dependsOnFocus [] true)
    (getFunctionName []
      (StructuredQName. "dr" our-uri "suggest-font-size"))
    (getArgumentTypes []
      (into-array SequenceType []))
    (getResultType [_] SequenceType/SINGLE_STRING)
    (makeCallExpression []
      (let [state (atom {})]
        (proxy [net.sf.saxon.lib.ExtensionFunctionCall]
            []
          (supplyStaticContext
            [static-ctx- loc-id- args-]
            (condp instance? static-ctx-
              ExpressionContext
              (swap! state assoc
                     :static-context static-ctx-
                     :location-id loc-id-
                     :arguments args-)))
          (call
            [context args]
            (let [src-node (.getContextItem context)
                  static-ctx (:static-context @state)
                  func-location (.getStyleElement static-ctx)]
              ;; Fill in this here.
              )))))))
