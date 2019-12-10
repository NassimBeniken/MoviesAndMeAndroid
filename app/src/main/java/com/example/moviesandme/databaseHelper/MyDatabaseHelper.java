package com.example.moviesandme.databaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.moviesandme.Model.Movie;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "androidApp";
    public static int DATABASE_VERSION = 1;

    public static final String MOVIE_TABLE = "MOVIES";
    public static final String MOVIE_ID = "id";
    public static final String MOVIE_TITLE = "title";
    public static final String MOVIE_YEAR = "year";
    public static final String MOVIE_RELEASED = "released";
    public static final String MOVIE_GENRE = "genre";
    public static final String MOVIE_IMAGE = "image";
    public static final String MOVIE_SYNOPSIS = "synopsis";

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String script = "CREATE TABLE " + MOVIE_TABLE + " (" +
                "id INTEGER PRIMARY KEY," +
                "title TEXT," +
                "year TEXT," +
                "released TEXT," +
                "genre TEXT," +
                "image TEXT," +
                "synopsis TEXT); ";
        db.execSQL(script);

        //Insertion de quelques films dans la base de données si jamais le premier appel à Heroku ne fonctionne pas

        Movie starwars = new Movie(1,"Star Wars: Episode IV - A New Hope","1977", "25 May 1977","Action, Adventure, Fantasy, Sci-Fi", "https://www.originalcomics.fr/3244/star-wars-episode-iv-un-nouvel-espoir.jpg","Il y a bien longtemps, dans une galaxie très lointaine. La guerre civile fait rage entre l'Empire galactique et l'Alliance rebelle. Capturée par les troupes de choc de l'Empereur menées par le sombre et impitoyable Dark Vador, la princesse Leia Organa dissimule les plans de l'Etoile Noire, une station spatiale invulnérable, à son droïde R2-D2 avec pour mission de les remettre au Jedi Obi-Wan Kenobi.");
        Movie harrypotter = new Movie(2,"Harry Potter and the Deathly Hallows: Part 2","2011", "15 Jul 2011","Adventure, Drama, Fantasy, Mystery","https://aws.vdkimg.com/film/3/2/2/0/322018_backdrop_scale_1280xauto.jpg", "Dans la 2e Partie de cet épisode final, le combat entre les puissances du bien et du mal de l'univers des sorciers se transforme en guerre sans merci. Les enjeux n'ont jamais été si considérables et personne n'est en sécurité. Mais c'est Harry Potter qui peut être appelé pour l'ultime sacrifice alors que se rapproche l'ultime épreuve de force avec Voldemort.");
        Movie pirates = new Movie(3,"Pirates of the Caribbean: The Curse of the Black Pearl","2003", "09 Jul 2003","Action, Adventure, Fantasy","https://vignette.wikia.nocookie.net/piratesdescaraibes/images/2/23/Pirates-des-caraibes-malediction-black-pearl.jpg/revision/latest?cb=20150227092839&path-prefix=fr", "Petite, Elizabeth Swann, la fille du gouverneur, a sauvé de la noyade Will Turner après le naufrage de son bateau. Les années ont passé, Will et Elizabeth ont grandi. Will est devenu forgeron à Port Royal, et Elizabeth se prépare à épouser le commodore Norrington. Cependant, la vie d'Elizabeth bascule lorsque le capitaine Barbossa et sa bande de pirates décident d'attaquer Port Royal et la prennent en otage. Malheureusement pour lui, Barbossa a commis deux erreurs.");
        Movie lotr = new Movie(4,"Lord of the rings : The fellowship of the ring", "2001", "19 Dec 2001", "Adventure, Drama, Fantasy, Mystery", "https://i.ytimg.com/vi/vvx7m22GGtA/maxresdefault.jpg", "Un jeune et timide Hobbit, Frodon Sacquet, hérite d'un anneau magique. Bien loin d'être une simple babiole, il s'agit d'un instrument de pouvoir absolu qui permettrait à Sauron, le Seigneur des ténèbres, de régner sur la Terre du Milieu et de réduire en esclavage ses peuples. Frodon doit parvenir jusqu'à la Crevasse du Destin pour détruire l'anneau.");
        Movie captain = new Movie(5,"Captain America: The Winter Soldier","2014","26 Mar 2014","Action, Adventure, Sci-Fi","https://www.masculin.com/wp-content/uploads/sites/2/article/8681/captain-america-2-soldat-hiver.jpg","Steve Rogers, plus connu sous le nom de Captain America, s'est adapté tant bien que mal à son nouvel environnement, et poursuit ses missions en tant qu'agent du S.H.I.E.L.D., l'agence militaire chargée d'assurer l'ordre international. Mais une organisation secrète aux desseins maléfiques a réussi à infiltrer le S.H.I.E.L.D. qu'elle gangrène de l'intérieur.");

        this.addMovies(starwars, db);
        this.addMovies(harrypotter, db);
        this.addMovies(pirates, db);
        this.addMovies(lotr, db);
        this.addMovies(captain, db);

    }

    public void addMovies(Movie movie, SQLiteDatabase db) {
        ContentValues values = new ContentValues();

        values.put(MOVIE_ID, movie.getId());
        values.put(MOVIE_TITLE, movie.getTitle());
        values.put(MOVIE_YEAR, movie.getYear());
        values.put(MOVIE_RELEASED, movie.getReleased());
        values.put(MOVIE_GENRE, movie.getGenre());
        values.put(MOVIE_IMAGE, movie.getImage());
        values.put(MOVIE_SYNOPSIS, movie.getSynopsis());

        db.insert(MOVIE_TABLE, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MOVIE_TABLE);
        onCreate(db);
        this.DATABASE_VERSION = newVersion;
    }
}
