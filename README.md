# docrenderer

This module accepts XML input and renders it to PDF using a configured
stylesheet.

## Configuration

Two environment variables must be set if not using the provided Dockerfile:

* `CONFIG_FILE` is the path of a configuration file; see `config.sample.toml`
for a sample file. (This value defaults to `/docrenderer/config.toml` in the
provided Dockerfile.)
* `STYLESHEETS_DIR` is a directory containing XSL stylesheets. All stylesheet
filenames in `CONFIG_FILE` are relative to this directory. (This value
defaults to `/docrenderer/stylesheets` in the provided Dockerfile.)

## Usage example

    docker build -t docrenderer:testme . && \
    docker stop docrenderer && \
    docker rm docrenderer && \
    docker run -d --name=docrenderer -p 127.0.0.1:3000:3000 \
      -v /some/stylesheets/directory:/docrenderer/stylesheets \
      -v /some/config/file.toml:/docrenderer/config.toml \
        docrenderer:testme

## Custom fonts

To use fonts beyond the Base 14, mount the directory containing the fonts
you wish to use into /usr/local/share/fonts on the host by adding a `-v`
option, e.g. `-v /some/host/directory/app/fonts:/usr/local/share/fonts`.

## License

Copyright © 2017 Brad Ackerman.

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
