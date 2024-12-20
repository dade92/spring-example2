db = new Mongo().getDB("customers");

db.createCollection('mongocustomer', {capped: false});

db.mongocustomer.createIndex({name: 1}, {unique: true});