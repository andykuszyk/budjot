FROM node:8.11.2-jessie
COPY ./dist/budjot/* app/dist/budjot/
COPY ./api/* app/api/
COPY ./package.json app/
WORKDIR app
RUN npm install
EXPOSE 8080
CMD node ./api/app.js 8080 mongoUser mongoPassword

