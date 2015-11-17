# candelabrum

Extract clippings from your Kindle.

## Usage

1. Add `[candelabrum "0.1.0"]` to dependencies.
2. `(require '[candelabrum.core :as candelabrum])`

Core namespace export only one function called `parse`, which takes
`java.io.Reader` and returns lazy sequence of parsed clippings.

```clojure
(with-open [r (clojure.java.io/reader "My Clippings.txt")]
  (clojure.pprint/pprint (candelabrum/parse r)))))

({:type :highlight,
  :added #inst "2015-10-20T11:35:45.000-00:00",
  :location {:type :location, :start 370, :end 371},
  :book {:title "Structure and Interpretation of Computer Programs, Second Edition",
         :author "Harold Abelson, Gerald Jay Sussman, Julie Sussman"},
  :text "programs must be written for people to read, and only incidentally for machines to execute."}
 {:type :highlight,
  :added #inst "2015-10-27T12:41:17.000-00:00",
  :location {:type :page, :start 114, :end 114},
  :book {:title "sicp"},
  :text "We are using here a powerful strategy of synthesis: wishful thinking"})
```

Note:

- `:book :author` might be absent
- `:type` is one of `[:highlight :note :bookmark]`
- `:location :type` is one of `[:page :location]`
- `:location :end` can be nil


## License

Copyright © 2015 Igor Bondarenko

Distributed under the MIT License.
