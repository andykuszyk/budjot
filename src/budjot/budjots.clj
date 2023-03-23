(ns budjot.budjots)

(defn handle-budjot-post [request]
  (let [body (:body request)]
    {:status 201
     :body {:name (:name body)
            :income (:income body)
            :userid (:userid body)
            :entries (:entries body)}}))
