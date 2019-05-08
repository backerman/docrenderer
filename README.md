# docrenderer

This module accepts XML input and renders it to PDF using a configured
stylesheet.

## Versioning

There's still a bit more work before I'm ready to tag a release, but
if someone else wants to use this image in production, let me know and
I'll tag it anyhow.

## Configuration

Two environment variables must be set if not using the provided
Dockerfile:

* `CONFIG_FILE` is the path of a configuration file; see
`config.sample.toml` for a sample file. (This value defaults to
`/docrenderer/config.toml` in the provided Dockerfile.)
* `STYLESHEETS_DIR` is a directory containing XSL stylesheets. All
stylesheet filenames in `CONFIG_FILE` are relative to this
directory. (This value defaults to `/docrenderer/stylesheets` in the
provided Dockerfile.)

## Custom fonts

To use fonts beyond the Base 14, mount the directory containing the
fonts you wish to use into /usr/local/share/fonts on the host.

## Usage example

N.B.: Docker bind mounts must be specified with their absolute
pathnames.

    docker build -t docrenderer:testme .
    docker stop docrenderer
    docker rm docrenderer
    docker run -d \
    --mount=type=bind,source=/some/path/config.toml,destination=/docrenderer/config.toml \
    --mount=type=bind,source=/some/path/myxsls,destination=/docrenderer/stylesheets \
    --mount=type=bind,source=/some/path/myfonts,destination=/usr/local/share/fonts \
    --name=docrenderer -p 3000:3000 backerman/docrenderer

## License

Copyright © 2017&ndash;9 Brad Ackerman.

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
