# async-tasks-client

A client library for the CyVerse Discovery Environment async-tasks service.

## Usage

(currently copied from metadata-client, needs updating)

```clojure
(require '[async-tasks-client.core :as mcc])

(def client (mcc/new-metadata-client base-uri))

;; Metadata AVU operations.
(mcc/find-avus client username {:attribute attribute :target-type target-type :value value :unit unit})
(mcc/find-avus client username {:attribute [attr1 attr2] :target-type [tt1 tt2] :value [v1 v2] :unit [u1 u2]})
(mcc/list-avus client username target-type target-id)
(mcc/update-avus client username target-type target-id request-body)
(mcc/set-avus client username target-type target-id request-body)
(mcc/copy-metadata-avus client username target-type target-id dest-targets)
(mcc/filter-by-avus client username target-types target-ids avus)
(mcc/delete-avus client username target-types target-ids avus)

;; Ontology operations.
(mcc/list-ontologies client username)
(mcc/list-hierarchies client username ontology-version)
(mcc/filter-hierarchies client username ontology-version attrs target-type target-id)
(mcc/filter-targets-by-ontology-search client username ontology-version attrs search-term target-types target-ids)
(mcc/filter-hierarchy client username ontology-version root-iri attr target-types target-ids)
(mcc/filter-hierarchy-targets client username ontology-version root-iri attr target-types target-ids)
(mcc/filter-unclassified client username ontology-version root-iri attr target-types target-ids)
(mcc/delete-ontology client username ontology-version)
```

## License

http://www.cyverse.org/sites/default/files/iPLANT-LICENSE.txt
