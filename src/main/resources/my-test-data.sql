INSERT INTO rooms (capacity, pricePerDay) VALUES (2, 10.000);
INSERT INTO persons (name, phoneNumber, address) VALUES ('Brontofus', '12433544', 'Za dedinou');
INSERT INTO rents (price, roomId, personId, startDay, expectedEndDay, realEndDay, countOGuestsInRoom) VALUES (null, 1, 1, '2014-04-07', '2014-04-11', null, 1);