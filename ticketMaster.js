var geohash = require('ngeohash');
var request = require('request');
var express = require('express');
var router = express.Router();

const key = 'zLehLv7rzhAKTRNyEc3aPvzwtyo5NTCT';

function getEventsByKeywod({
		keyword,
		categoryid,
		distance,
		geoHashVal
	}, callback){
	if (categoryid == '') {
		url = "https://app.ticketmaster.com/discovery/v2/events.json?apikey="+key+"&keyword="+keyword+"&radius="+distance+"&unit=miles&geoPoint="+geoHashVal;
		url = url.replace(/ /g, '+');
	} else {
		url = "https://app.ticketmaster.com/discovery/v2/events.json?apikey="+key+"&keyword="+keyword+"&segmentId="+categoryid+"&radius="+distance+"&unit=miles&geoPoint="+geoHashVal;
		url = url.replace(/ /g, '+');
	}
	console.log(url);
	request.get(url, function(error, res, body){
  		callback(JSON.parse(body));
	});
};

router.get('/getEvents', function(req, res, next){
	var keyword = req.query.keyword;
	var category = req.query.category;
	var distance = req.query.distance;
	var unit = req.query.unit;
	var lat = req.query.lat;
	var lon = req.query.lon;
	var geoHashVal = geohash.encode(lat, lon);
	var categoryid = '';
	if (category == 'Music') {
		categoryid = 'KZFzniwnSyZfZ7v7nJ';
	}
	if (category == 'Sports') {
		categoryid = 'KZFzniwnSyZfZ7v7nE';
	}
	if (category == 'ArtsAndTheatre') {
		categoryid = 'KZFzniwnSyZfZ7v7na';
	}
	if (category == 'Film') {
		categoryid = 'KZFzniwnSyZfZ7v7nn';
	}
	if (category == 'Miscellaneous') {
		categoryid = 'KZFzniwnSyZfZ7v7n1';
	}
	if (unit == 'kilometers') {
		distance = Math.round(distance * 0.621371);
	}
	getEventsByKeywod({
		keyword,
		categoryid,
		distance,
		geoHashVal
	}, function(ret){
		console.log(ret);
		res.send(ret);
	})
});

function getDetail(id, callback){
	var url = "https://app.ticketmaster.com/discovery/v2/events/"+id+"?apikey="+key;
	url = url.replace(/ /g, '+');
	console.log(url);
	request.get(url, function(error, res, body){
		callback(JSON.parse(body));
	});
}

router.get('/getDetail', function(req, res, next){
	var id = req.query.eventId;
	getDetail(id, function(ret){
		res.send(ret);
	});
});

function getVenueInfo(name, callback){
	var url = "https://app.ticketmaster.com/discovery/v2/venues?apikey="+key+"&keyword="+name;
	url = url.replace(/ /g, '+');
	request.get(url, function(error, res, body){
		callback(JSON.parse(body));
	});
}

router.get('/getVenue', function(req, res, next){
	var name = req.query.veneuName;
	getVenueInfo(name, function(ret){
		res.send(ret);
	});
});

function getAutocomplete(keyword, callback){
	var url = "https://app.ticketmaster.com/discovery/v2/suggest?apikey="+key+"&keyword="+keyword;
	url = url.replace(/ /g, '+');
	console.log(url);
	request.get(url, function(error, res, body){
		callback(JSON.parse(body));
	});
}


router.get('/autocomplete', function(req, res, next){
	var keyword = req.query.keyword;
	console.log(keyword);
	getAutocomplete(keyword, function(ret){
		res.send(ret);
	});
});

module.exports = router;



















