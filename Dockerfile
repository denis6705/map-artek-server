FROM clojure
COPY . /usr/src/app
WORKDIR /usr/src/app
EXPOSE 80
CMD ["lein", "run"]