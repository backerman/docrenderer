FROM clojure
MAINTAINER Brad Ackerman "brad@facefault.org"

# Download fonts and non-Java programs we need.
RUN apt-get update && apt-get install -y --no-install-recommends \
                        fonts-junction pdfjam texlive-latex-recommended \
  && rm -rf /var/lib/apt/lists/*

# Set up application directory.
ENV APPDIR /bidsheets
RUN mkdir -p "$APPDIR" && chmod -R 775 "$APPDIR"

# Download dependencies.
COPY project.clj "$APPDIR"
WORKDIR "$APPDIR"
RUN lein deps
COPY . "$APPDIR"
RUN mv "$(lein uberjar | sed -n 's/^Created \(.*standalone\.jar\)/\1/p')" app-standalone.jar
CMD ["java", "-jar", "app-standalone.jar"]
EXPOSE 3000