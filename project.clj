(defproject org.facefault/docrenderer "0.6.0-SNAPSHOT"
  :description "Render XSL to PDF."
  :url "https://github.com/backerman/docrenderer"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins [[lein-ring "0.11.0"]
            [lein-environ "1.1.0"]
            [lein-cljfmt "0.5.6"]]
  :ring {:handler org.facefault.docrenderer.core/handler}
  :profiles {
             :uberjar {:aot :all}
             }
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [ring/ring-core "1.5.1"]
                 [ring/ring-jetty-adapter "1.5.1"]
                 [ring/ring-defaults "0.2.3"]
                 [ring-logger "0.7.7"]
                 [compojure "1.5.2"]
                 [net.sf.saxon/Saxon-HE "9.7.0-18"]
                 [org.clojure/tools.logging "0.3.1"]
                 [org.apache.logging.log4j/log4j-api "2.8.2"]
                 [org.apache.logging.log4j/log4j-core "2.8.2"]
                 [org.apache.xmlgraphics/fop "2.2"]
                 ;; these Batik JARs are required for FOP but not
                 ;; in FOP's dependencies.
                 [org.apache.xmlgraphics/batik-i18n "1.9"]
                 [org.apache.xmlgraphics/batik-constants "1.9"]
                 [org.apache.pdfbox/fontbox "2.0.5"]
                 [me.raynes/conch "0.8.0"]
                 [me.raynes/fs "1.4.6"]
                 [environ "1.1.0"]
                 [clj-toml "0.3.1"]
                 ]
  :main org.facefault.docrenderer.core)
