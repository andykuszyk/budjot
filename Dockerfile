FROM node:8.11.2-jessie
COPY ./dist/budjot/* app/
WORKDIR app
RUN npm install http-server -g
EXPOSE 8080
CMD http-server ./

