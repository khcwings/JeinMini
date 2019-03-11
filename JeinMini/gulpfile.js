/**
 * Gulp build script
 * Author : jeinsoft
 * Description : 
 * gulp -> gulp workflow core module
 * concat -> bundle file creator
 * uglify -> obfuscation and compression
 * 
 * ## javascript build
 * $ gulp grid -> grid script gulp
 * $ gulp combobox -> combobox script gulp
 * $ gulp build -> all script gulp
 * 
 * ## 
 */

var gulp = require('gulp');
var concat = require('gulp-concat');
var uglify = require('gulp-uglify');
var del = require('del');
var debug = require('gulp-debug');

var path = {
    grid : ['src/main/resources/static/js/common/jein-grid-1.0.0.js']
    , combobox : ['src/main/resources/static/js/common/jein-combobox-1.0.0.js']
	, modal : ['src/main/resources/static/js/common/jein-modal-1.0.0.js']
	, core : [
		'src/main/resources/static/js/common/jein-grid-1.0.0.js'
		, 'src/main/resources/static/js/common/jein-combobox-1.0.0.js'
		, 'src/main/resources/static/js/common/jein-modal-1.0.0.js'
	]
    , libraries : []
};
var dist = 'src/main/resources/static/dist';

gulp.task('clean', function(cb){
    del([dist], cb);
});

gulp.task('grid', function(){
    return gulp.src(path.grid)
    	.pipe(debug({title : 'debugging'}))
        .pipe(concat('jein-grid-1.0.0.min.js'))
        .pipe(uglify())
        .pipe(gulp.dest(dist + '/src'));
});

gulp.task('combobox', function(){
    return gulp.src(path.combobox)
    	.pipe(debug({title : 'debugging'}))
        .pipe(concat('jein-combobox-1.0.0.min.js'))
        .pipe(uglify())
        .pipe(gulp.dest(dist + '/src'));
});

gulp.task('modal', function(){
    return gulp.src(path.modal)
    	.pipe(debug({title : 'debugging'}))
        .pipe(concat('jein-modal-1.0.0.min.js'))
        .pipe(uglify())
        .pipe(gulp.dest(dist + '/src'));
}); 

gulp.task('core', function(){
    return gulp.src(path.core)
    	.pipe(debug({title : 'debugging'}))
        .pipe(concat('jein-core-1.0.0.min.js'))
        .pipe(uglify())
        .pipe(gulp.dest(dist + '/src'));
}); 

gulp.task('build', ['grid', 'combobox', 'modal']);
gulp.task('zip', ['core']);

gulp.task('default', function() {
  // place code for your default task here
});