FROM node:14-alpine
WORKDIR /app

COPY . .
RUN node --version
RUN npm install

CMD ["npm", "run", "dev"]