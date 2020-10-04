FROM node:8.11.2-jessie
COPY ./dist/budjot/* app/dist/budjot/
COPY ./dist/budjot/assets/* app/dist/budjot/assets/
COPY ./api/* app/api/
COPY ./package.json app/
WORKDIR app
RUN npm install
CMD node ./api/app.js 80 $MONGOURL

