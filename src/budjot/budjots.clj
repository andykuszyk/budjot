(ns budjot.budjots)

(defn handle-budjot-post [request]
  {
   :status 201
   :body {
          :name (:name request)
          }})
