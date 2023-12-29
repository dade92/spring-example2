db = new Mongo().getDB("customers");

db.createCollection('mongocustomer', {capped: false});

db.mongocustomer.insert([
    {
        "_id": '1',
        "name": "Davide",
        "age": 31,
        "favouriteDestinations": {destinations: [{city: 'Milan'}, {city: 'London'}]},
        "creationDate": '2023-12-29T08:48:06.742Z'
    },
    {
        "_id": '2',
        "name": "Sergio",
        "age": 62,
        "favouriteDestinations": {destinations: [{city: 'Milan'}, {city: 'London'}]},
        "creationDate": '2023-12-29T08:48:06.742Z'
    },
]);