 jQuery('document').ready(function($) {
	'use strict';

	$("body").addClass('loaded');
	console.log("Scripts loaded!");

	let endpoint = 'https://api.themoviedb.org/3/discover/movie';
	let movieEndpoint = 'https://api.themoviedb.org/3/movie/'
	let apiKey = 'ca3d69ee336e43d8099727f0d7ce3859';
	let movieId = '';

	// Define pre-selected filters
	let year = "2020";
	let genre = ""; // No genre selected, display all

	// Ajax request to the movie database; Hide all previous entries with .remove() and render new ones when thsi function is called
	function doRequest(selectedYear, selectedGenre) {
		$('.movie-item').remove();
		
		$.ajax({
			url: endpoint + "?api_key=" + apiKey +"&year=" + selectedYear + "&with_genres=" + selectedGenre,
			contentType: "application/json",
			dataType: 'json',
			success: function(result){
				jQuery.each(result.results, function(index, item) {
	
					let voteRating = item.vote_average;
					let recommendation;
	
					// If movie rating is below 7, show a thumbs down icon
					if (voteRating <= 7) {
						recommendation = "dist/images/thumbs-down-solid.svg";
					
					// Else show a thumbs up icon
					} else {	
						recommendation = "dist/images/thumbs-up-solid.svg";
					}
	
					$('.movies-list').append(`
						<li class="movie-item">
							<a class="movie-item-link" href="movie.html?id=${item.id}">
								<div class="movie-rating"><span>${item.vote_average}</span><img class="movie-recommendation" src="${recommendation}" alt=""/></div>
								<aside class="movie-thumbnail">
									<picture><img src="https://image.tmdb.org/t/p/w440_and_h660_face/${item.poster_path}"></picture>
								</aside>
								<div class="movie-meta">
									<p class="movie-release-date">${item.release_date}</p>
									<h2 class="movie-title">${item.title}</h2>
									<div class="movie-description">${item.overview}</div>
								</div>
							</a>
						</li>
					`);
				});
			}
		});
	}

	// Get single movie
	function getMovie() {
		let params = (new URL(document.location)).searchParams;
		let getId = params.get('id');
		movieId = getId;

		$.ajax({
			url: movieEndpoint + movieId + "?api_key=" + apiKey,
			contentType: "application/json",
			dataType: 'json',

			success: function(result){
				//console.log(result);

				let voteRating = result.vote_average;
				let recommendation;

				let date = new Date(result.release_date);
				
				$('.single-movie-poster').append(`<img src="https://image.tmdb.org/t/p/w1920_and_h800_multi_faces/${result.poster_path}">`);
				$('.single-movie-thumbnail').append(`<picture><img src="https://image.tmdb.org/t/p/w440_and_h660_face/${result.poster_path}"></picture>`)
				$('.single-movie-title').prepend(`${result.title}` + "<span class='single-movie-date'>(" + date.getFullYear() + ")</span>");
				$('.single-movie-overview').append(`${result.overview}`);

				let genresList = [];
				jQuery.each(result.genres, function(index, item) {
					return genresList.push(String(item.name).split(',')[0]);
				});
				
				$('.single-movie-details .runtime').prepend(result.runtime);
				$('.single-movie-details .genres').append(genresList.join(', '));
				$('.movie-id').val(movieId);
				getFavoriteMovies();
			}
		});
	}

	// Get all comments from single movie
	function getMovieComments() {
		let params = (new URL(document.location)).searchParams;
		let getId = params.get('id');

		$.ajax({
			url: "/comment/all",
			method: "GET",
			contentType: "application/json",
			dataType: 'json',

			complete : function(data){ 	
				jQuery.each(data.responseJSON, function(index, item) {
					
					if (item.movieId == getId) {
						$('.movie-comments-list').append(`
							<li class="comment-item">
								<picture class="comment-author-avatar"><img src="https://upload.wikimedia.org/wikipedia/commons/7/7c/Profile_avatar_placeholder_large.png" alt=""></picture>
								<div class="comment-author-meta">
									<h4 class="comment-author">${item.user.username}</h4>
									<span class="comment-rating">Rating: ${item.rating}/10</span>
									<div class="comment-copy">${item.comment}</div>
								</div>
							</li>
						`)
					}
				});
			},fail : function(){
				console.log("Failed getting comments");
			}
		});
	}

	function postComment(data){
        	
		let movieId = $('.movie-id').val();
		let commentToSend = $('#comment').val();
		let rating = $('#rating option:selected').text();
			
		$.ajax({
			url: "/comment/post",
			method: "POST",
			data : {
				commentarea : commentToSend,
				movieId : movieId,
				rating : parseInt(rating),
			},
			success : function(response){
				if(response.includes("Error:")){
					alert(response);
				}else{
					$('.movie-comments-list').append(`
						<li class="comment-item is-highlighted">
							<picture class="comment-author-avatar"><img src="https://upload.wikimedia.org/wikipedia/commons/7/7c/Profile_avatar_placeholder_large.png" alt=""></picture>
							<div class="comment-author-meta">
								<h4 class="comment-author">Me</h4>
								<span class="comment-rating">Rating: ${rating}/10</span>
								<div class="comment-copy">${commentToSend}</div>
							</div>
						</li>
					`);				       				
				 }
			},
			fail : function(){
				alert("Something went terribly wrong!!!");
			}
			
		})
	}

	function favoritesAdd(data){
		let movieId = $('.movie-id').val();
			
		$.ajax({
			url: "/favorites/add",
			method: "POST",
			data : {
				movieId : movieId,
			},
			success : function(response){
				console.log(response);
				if(response.includes("Error:")){
					alert(response);
				}else{	
					$('.add-to-favorite').addClass('is-favorite');       				
				}      			
			},
			fail : function(){
				alert("Something went terribly wrong!!!");
			}
		});
	}

	//Get all favorite movies
	function getFavoriteMovies() {
		let movieId = $('.movie-id').val();

		$.ajax({
			url: "/favorites/all",
			method: "GET",
			contentType: "application/json",
			dataType: 'json',

			complete : function(data){ 	
				jQuery.each(data.responseJSON, function(index, item) {
					//favoriteMovies(item.movieId, item.user.id);

					if (item.movieId == movieId && item.user.id == myId) {
						$('.add-to-favorite').addClass("is-favorite")
					}
				});
			},fail : function(){
				console.log("Failed getting favorite movies");
			}
		});
	}
	
	function favoriteMovies(movieID, userID){
		let movieId = $('.movie-id').val();

		if ( me == userID ) {
			if (movieId == movieID) {
				
			}
		} 
	}

	function favoritesRemove(favorite, id){
		let movieId = $('.movie-id').val();

		$.ajax({
			url: "/favorites/remove",
			method: "DELETE",
			data : {
				id : id,
				movieId : movieId,
			},
			complete : function(data){
				console.log(data);
					$('.add-to-favorite').removeClass("is-favorite");				       				     			
			}
		});
	}

	let me;
	let myId;
	getMyId();
	function getMyId(){
		$.ajax({
			url : "/whoAmI",
			method : "GET",
			complete : function(data){ 	
				switch(data.status){
				case 200:
					myId = data.responseJSON;
				}
			}
		});
	}

	function getWhoAmI(){
		$.ajax({
			url : "/whoAmI",
			method : "GET",
			complete : function(data){ 	
				switch(data.status){
				case 200:
					me = data.responseJSON;
					break;
				case 401 :
					window.location.href="index.html";
					break;
				}  				

			},fail : function(){
				window.location.href="index.html";
			}
		});
	}

	$('.add-to-favorite').click(function(){
		if (! $(this).hasClass('is-favorite')) {
			favoritesAdd();
		}
	});

	$('.comments-form .btn').click(function(e){
		e.preventDefault();
		postComment();

		$('#comment').val("");
	});

	if ($('body').hasClass('page-movie')) {
		getMovie();
	} else {
		// Initial load of the movies list, Pre-selected Year 2020 - Action
		doRequest(year, genre);
	}

	if ( ! $('body').hasClass('page-login')) {
		if ($('body').hasClass('page-error')) {
			setTimeout(() => {
				getWhoAmI();
			}, 2000);
		} else if ($('body').hasClass('page-register')) {
			//donothing
		} else {
			getWhoAmI();
		}
	}

	if ($('body').hasClass('page-movie')) {
		getMovieComments();
	}
});