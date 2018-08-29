/* Add example products */
INSERT INTO products(name, description, baseprice, amount, unit) VALUES (
  'Juhla Mokka',             /*name*/
  'Tekijöiden kahvi',        /*description*/
  5.0,                       /*baseprice*/
  1337,                      /*quantity*/
  'kg'
);

INSERT INTO products(name, description, baseprice, amount, unit) VALUES (
  'Juhla Mokka Tumma',        /*name*/
  'Tekijöiden kahvi',         /*description*/
  4.8,                        /*baseprice*/
  1000,                       /*quantity*/
  'kg'
);

/* Add example users */
INSERT INTO users(name, password, description, locked) VALUES (
  'Aleksi',                         /*name*/
  'jaahas',							/*password*/
  'Peruskäyttäjä',                  /*description*/
  false                             /*locked*/
);

INSERT INTO users(name, password, description, locked) VALUES (
  'Heikki',                         /*name*/
  'blackhat',			            /*password*/
  'Peruskäyttäjä',                  /*description*/
  false                             /*locked*/
);

INSERT INTO users(name, password, description, locked) VALUES (
  'Toni',                           /*name*/
  'tervehdys',                      /*password*/
  'Peruskäyttäjä',                  /*description*/
  false                             /*locked*/
);

/* Add example clients */
INSERT INTO clients(name, description) VALUES (
  'Leela',                          /*name*/
  'Ohjelmointiguru'                 /*description*/
);

INSERT INTO clients(name, description) VALUES (
  'Kari',                          /*name*/
  'Hoitaa homman kotiin'           /*description*/
);

INSERT INTO clients(name, description) VALUES (
  'Tapio',                          /*name*/
  'Kivenkova ammattilainen'         /*description*/
);

/* Add example transactions */
INSERT INTO transactions(userid, clientid, description, productid, amount, price) VALUES (
  7,                          /*userid*/
  8,                          /*clientid*/
  'Hyvä kauppa oli',          /*description*/
  3,                          /*productid*/
  7,                          /*amount*/
  10.33                       /*price*/
);

INSERT INTO transactions(userid, clientid, description, productid, amount, price) VALUES (
  9,                          /*userid*/
  7,                          /*clientid*/
  'Huono kauppa oli',         /*description*/
  2,                          /*productid*/
  2,                          /*amount*/
  0.33                        /*price*/
);
