(defproject org.facefault/docrenderer "0.6.0-SNAPSHOT"
  :description "Render XSL to PDF."
  :url "https://github.com/backerman/docrenderer"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins [[lein-ring "0.12.5"]
            [lein-environ "1.1.0"]
            [lein-cljfmt "0.6.4"]]
  :ring {:handler org.facefault.docrenderer.core/handler}
  :profiles {
             :uberjar {:aot [org.facefault.docrenderer.main]}
             :repl {:dependencies [[org.clojure/tools.namespace "0.2.11"]]}
             }
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [ring/ring-core "1.7.1"]
                 [ring/ring-jetty-adapter "1.7.1"]
                 [ring/ring-defaults "0.3.2"]
                 [ring-logger "1.0.1"]
                 [compojure "1.6.1"]
                 [net.sf.saxon/Saxon-HE "9.7.0-18"]
                 [org.clojure/tools.logging "0.4.1"]
                 [org.apache.logging.log4j/log4j-api "2.8.2"]
                 [org.apache.logging.log4j/log4j-core "2.8.2"]
                 [org.apache.xmlgraphics/fop "2.3"
                  :exclusions [commons-logging]]
                 ;; these Batik JARs are required for FOP but not
                 ;; in FOP's dependencies.
                 [org.apache.xmlgraphics/batik-i18n "1.10"]
                 [org.apache.xmlgraphics/batik-constants "1.10"]
                 [org.apache.pdfbox/fontbox "2.0.15"]
                 [me.raynes/conch "0.8.0"]
                 [me.raynes/fs "1.4.6"]
                 [environ "1.1.0"]
                 [clj-toml "0.3.1"]
                 ]
  :main org.facefault.docrenderer.main)
