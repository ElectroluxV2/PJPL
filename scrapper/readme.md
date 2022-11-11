# PJPL Scrapper
## Local environment setup
- Use java 19 at least
- Set ENV variable `PJPL_storage` to absolute path pointing to output location.
- Scrapper will run `docker run --rm -d --name chromium_3xxx -p 127.0.0.1:3xxx:4444 -e SE_SESSION_REQUEST_TIMEOUT=36000 -e SE_NODE_SESSION_TIMEOUT=36000 -e SE_NODE_MAX_SESSIONS=8 -e SE_START_XVFB=false -m 2G --shm-size 2g docker.io/seleniarm/standalone-chromium:latest` underneath
