db = new Mongo().getDB("customers");

db.createCollection('mongocustomer', { capped: false });

db.mongocustomer.insert([
    { "_id": '1', "name": "Davide", "age": 31, "favouriteDestinations": {destinations: [{city: 'Milan'}, {city: 'London'}]} },
    { "_id": '2', "name": "Sergio", "age": 62, "favouriteDestinations": {destinations: [{city: 'Milan'}, {city: 'London'}]} },
]);