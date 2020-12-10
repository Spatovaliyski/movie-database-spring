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

	function getMovie() {
		let params = (new URL(document.location)).searchParams;
		let getId = params.get('id');
		movieId = getId;
		console.log(movieId);

		$.ajax({
			url: movieEndpoint + movieId + "?api_key=" + apiKey,
			contentType: "application/json",
			dataType: 'json',

			success: function(result){
				console.log(result);

				let voteRating = result.vote_average;
				let recommendation;

				let date = new Date(result.release_date);
				
				$('.single-movie-poster').append(`<img src="https://image.tmdb.org/t/p/w1920_and_h800_multi_faces/${result.poster_path}">`);
				$('.single-movie-thumbnail').append(`<picture><img src="https://image.tmdb.org/t/p/w440_and_h660_face/${result.poster_path}"></picture>`)
				$('.single-movie-title').append(`${result.title}` + "<span class='single-movie-date'>(" + date.getFullYear() + ")</span>");
				$('.single-movie-overview').append(`${result.overview}`);

				let genresList = [];
				jQuery.each(result.genres, function(index, item) {
					return genresList.push(String(item.name).split(',')[0]);
				});
				
				$('.single-movie-details .runtime').prepend(result.runtime);
				$('.single-movie-details .genres').append(genresList.join(', '));
				$('.movie-id').val(movieId);
			}
		});
	}

	let me;

	// function getWhoAmI(){
	// 	$.ajax({
	// 		url : "/whoAmI",
	// 		method : "GET",
	// 		complete : function(data){ 	
	// 			switch(data.status){
	// 			case 200:
	// 				me = data.responseJSON;
	// 				break;
	// 			case 401 :
	// 				window.location.href="index.html";
	// 				break;
	// 			}  				

	// 		},fail : function(){
	// 			window.location.href="index.html";
	// 		}
	// 	});
	// }

	$('#logout').on('click', function(){
		$.ajax({
			url: "logout",
			method: "POST",
			complete : function(data){
				if(data.status == 401){
					alert("ERROR!");
				}

				window.location.href = "index.html";
			}
		})		
	});

	if ($('body').hasClass('page-movie')) {
		getMovie();
	} else {
		// Initial load of the movies list, Pre-selected Year 2020 - Action
		doRequest(year, genre);
	}

	if ( ! $('body').hasClass('page-login')) {
		//getWhoAmI();
	}

	//getWhoAmI();
});