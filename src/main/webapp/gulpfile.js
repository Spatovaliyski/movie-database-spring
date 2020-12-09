"use strict";

let gulp = require("gulp"),
	autoprefixer = require("gulp-autoprefixer"),
	livereload = require("gulp-livereload"),
	sass = require("gulp-sass"),
	notify = require("gulp-notify"),
	uglify = require("gulp-uglify"),
	concat = require("gulp-concat"),
	cleanCSS = require("gulp-clean-css"),
	rename = require("gulp-rename"),
	imagemin = require("gulp-imagemin"),
	del = require("del"),
	moment = require("moment"),
	sassLint = require("gulp-sass-lint"),
	newer = require("gulp-newer"),
	sourcemaps = require("gulp-sourcemaps"),
	babel = require("gulp-babel"),
	plumber = require("gulp-plumber"),
	cp = require("child_process");

const paths = {
	source: {
		scripts: "src/js/",
		sass: "src/scss/",
	},
	destination: {
		scripts: "dist/js/",
		css: "dist/css/",
	}
};

gulp.task("sass", function() {
	return gulp
	.src(paths.source.sass + "**/*.scss")
	.pipe(sourcemaps.init())
	.pipe(sass().on("error", sass.logError))
	.pipe(autoprefixer())
	.pipe(sourcemaps.write('./'))
	.pipe(gulp.dest(paths.destination.css))
	.pipe(notify({
		onLast: true,
		title: "Sass compiled successfully.",
		message: getFormatDate()
	}));
});

gulp.task("cssmin", function() {
	return gulp
	.src(paths.destination.css + "bootstrap.css")
	.pipe(sourcemaps.init({ loadMaps: true }))
	.pipe(cleanCSS({compatibility: 'ie8'}))
	.pipe(rename({ suffix: ".min" }))
	.pipe(sourcemaps.write('./'))
	.pipe(gulp.dest(paths.destination.css))
	.pipe(notify({ message: "Minified the CSS files!",onLast: true }))
});

gulp.task("watch", function() {
	gulp.watch(paths.source.sass + "**/*.scss", gulp.series("sass"));
	gulp.watch(paths.source.scripts + "**/*.js", gulp.series("minifyScripts"));

	gulp.watch(paths.destination.css + "bootstrap.css", gulp.series("cssmin"));
});

gulp.task("minifyScripts", function() {
	return gulp
	.src([
		paths.source.scripts + "vendor/*.js",
		paths.source.scripts + "inc/*.js",
		paths.source.scripts + "scripts.js"
	])
	.pipe(plumber({
		handleError: function (error) {
			console.log(error);
			this.emit('end');
		}
	}))
	.pipe(babel({
		presets: ['@babel/preset-env']
	}))
	.pipe(concat("bundle.min.js"))
	.pipe(uglify())
	.pipe(gulp.dest(paths.destination.scripts));
});

gulp.task("cleanup", function() {
	del(paths.destination.scripts + "bundle.min.js");
	del(paths.destination.css + "*.css");
});

gulp.task("reset", function() {
	del(".git");
	del(".DS_Store");
});

gulp.task("default", 
	gulp.series("sass",
		gulp.parallel("minifyScripts",
			"cssmin"),
		"watch"
	)
);

function getFormatDate() {
	return moment().format("LTS");
}