FROM node:14.17.6-alpine3.13
WORKDIR /app
RUN chown -R node:node /app
COPY --chown=node . .
USER node
HEALTHCHECK --start-period=5s \
    CMD wget --no-verbose --tries=1 --spider http://localhost:4000 || exit 1
ENTRYPOINT ["node", "server/main.js"]

ARG NAME=unknown
ARG VERSION=unknown
ARG DESCRIPTION=unknown
ARG VCS_REF=unknown
ARG VCS_URL=unknown

LABEL org.label-schema.name="${NAME}" \
      org.label-schema.version="${VERSION}" \
      org.label-schema.description="${DESCRIPTION}" \
      org.label-schema.vcs-ref="${VCS_REF}" \
      org.label-schema.vcs-url="${VCS_URL}" \
      org.label-schema.schema-version="1.0"
