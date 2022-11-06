# PJPL Scrapper
## Local environment setup
- Use java 19 at least
- Run `docker run --rm -d --name webdriver -p 127.0.0.1:3000:4444 -p 5900:5900 -p 7900:7900 -e SE_NODE_MAX_SESSIONS=8 -e SE_START_XVFB=false -m 10G --shm-size 2g docker.io/seleniarm/standalone-chromium:latest`
- Set ENV variable `PJPL_storage` to absolute path pointing to output location.
- Set ENV variable `PJPL_webdriver` to `http://localhost:3000`
- After local development run `docker stop webdriver`
