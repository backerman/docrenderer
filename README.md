# docrenderer

This module accepts XML input and renders it to PDF using a configured
stylesheet.

## Configuration

Two environment variables must be set:

* `CONFIG_FILE` is the path of a configuration file; see `config.sample.toml`
for a sample file.
* `STYLESHEETS_DIR` is a directory containing XSL stylesheets. All stylesheet
filenames in `CONFIG_FILE` are relative to this directory.

## Usage

`docker build -t docrenderer:latest . && docker stop docrenderer && docker rm docrenderer && docker run -d --name=docrenderer -p 127.0.0.1:3000:3000 docrenderer:latest`

## License

Copyright © 2017 Brad Ackerman.

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
