// Playlist.js

import React, { useEffect, useRef, useState } from "react";
import axios from "axios";
import "./Playlist.css";
import { doc, setDoc, deleteDoc } from "firebase/firestore";
import { db } from "../../src/firebase";

function Playlist({ username }) {
    const [playlistItems, setPlaylistItems] = useState([]);
    const [playlist, setPlaylist] = useState(false);
    const [playlistTracks, setPlaylistTracks] = useState([]);
    const [selectedPlaylist, setSelectedPlaylist] = useState(null);
    const [displayPlay, setDisplay] = useState(true);
    const [selectedArtistInfo, setSelectedArtistInfo] = useState(null);
    const [showArtistInfo, setShowArtistInfo] = useState(false);
    const [selectedSongs, setSelectedSongs] = useState([]);
    const [authorTrack, setAuthorTrack] = useState([]);
    const [authorTrack2, setAuthorTrack2] = useState([]);

    const [displaySearch, setDisplaySearch] = useState(true);
    const [inputValue, setInputValue] = useState('');
    const [authorId, setAuthorId] = useState("");
    const [pop, setPop] = useState("");
    const [selectedGenres, setSelectedGenres] = useState([]);
    const [recommendedSongs, setRecommendedSongs] = useState([]);
    const [playlistCreated, setPlaylistCreated] = useState(false);
    const [href,setHref] = useState("");
    const genres = ["acoustic", "alt-rock", "alternative", "blues", "bossanova", "classical", "country", "dance", "disco", "dubstep",
        "edm", "electronic", "folk", "funk", "grunge", "hard-rock", "heavy-metal", "hip-hop", "house", "indie", "jazz", "k-pop", "latino",
        "metal", "pop", "punk", "r-n-b", "reggae", "reggaeton", "rock", "rock-n-roll", "sertanejo", "ska", "soul", "synth-pop", "techno", "trance",
    ];
    useEffect(() => {
        axios.post("http://localhost:8080/likedSongs", {
            "username": window.localStorage.getItem("email"),
            "playlistId": {
                "creatorUsername": window.localStorage.getItem("creatorUsername"),
                "playlistName": window.localStorage.getItem("playlistName")
            }
        }, {
            headers: {
                'Content-Type': 'application/json'
            }
        }).then((response) => {
            console.log(response.data);
            setSelectedPlaylist(response.data)
            setSelectedSongs(response.data)
      });

    }, []);
    const [selectedOption, setSelectedOption] = useState(null);
    const options = ['Option 1', 'Option 2', 'Option 3', 'Option 4'];
    const [rangeValues, setRangeValues] = useState({ min: 1, max: 100 });

    const handleSelect = (event) => {
        setSelectedOption(event.target.value);
    };

    const handleChange = (e) => {
        setInputValue(e.target.value);
    };
    const [email, setEmail] = useState(window.localStorage.getItem("email"));
    const renderPlaylist = () => {
        return playlistItems.map((item) => (
            <div key={item.id} className="playlist-item">
                <div className="playlist-info">
                    <div className="playlist-name">{item.name}</div>
                    <div className="playlist-track-count">
                        Number of tracks in playlist: {item.tracks.total}
                    </div>
                </div>
                <button onClick={() => getTracks(item.id)}>See Tracks</button>
            </div>
        ));
    };

    const handleCheckboxChange = (genre) => {
        if (selectedGenres.includes(genre)) {
            setSelectedGenres(selectedGenres.filter(item => item !== genre));
        } else {
            setSelectedGenres([...selectedGenres, genre]);
        }
    };



    const getTracks = async (playlistId) => {
        let token = window.localStorage.getItem("token");
        const { data } = await axios.get(
            `https://api.spotify.com/v1/playlists/${playlistId}/tracks`,
            {
                headers: {
                    Authorization: "Bearer " + token,
                },
            }
        );

        setPlaylistTracks(data.items);
        setSelectedPlaylist(playlistId);
    };


    const renderTracks = () => {
        return playlistTracks.map((track) => (
            <div key={track.track.id} className="track-item">
                <img
                    src={track.track.album.images[0].url}
                    alt={`${track.track.name} Album Cover`}
                    className="track-image"
                />
                <div className="track-info">
                    <div className="track-popul">{track.track.popularity}</div>
                    <div className="track-name">{track.track.name}</div>
                    <div className="track-name"> 📈 {track.track.popularity}</div>
                    <div className="track-artist">
                        Artist: {track.track.artists[0].name}
                        {!selectedSongs.includes(track.track.id) && (
                            <button className="heart-button"
                                    onClick={() => addToSelectedSongs(track.track.id, track.track.artists[0].id, track.track.popularity)}>
                                ❤
                            </button>
                        )}
                        {selectedSongs.includes(track.track.id) && (
                            <button className="heart-button"
                                    onClick={() => removeFromSelectedSongs(track.track.id, track.track.artists[0].id, track.track.popularity)}>
                                ❤️
                            </button>
                        )}
                    </div>
                </div>
            </div>
        ));
    };
    const handleInputChange = (event, key) => {
        const value = parseInt(event.target.value, 10);

        /*
        // Ensure that the max value is always greater than the min value
        if (key === 'min' && value > rangeValues.max) {
            setRangeValues({ min: value, max: value });
        } else if (key === 'max' && value < rangeValues.min) {
            setRangeValues({ min: value, max: value });
        } else {
         */
        if (isNaN(value)) {
            if (key === 'min') setRangeValues({ ...rangeValues, [key]:  1});
            else setRangeValues({ ...rangeValues, [key]:  100});
        }
        else setRangeValues({ ...rangeValues, [key]: value });
        //}
    };

    function addToSelectedSongs(songId, author, popularity) {
        if (!selectedSongs.includes(songId)) {
            setSelectedSongs([...selectedSongs, songId]);
        }

        let response = axios.post("http://localhost:8080/addToPlaylist", {
            "song": {
                "songId": songId,
                "author": author,
                "popularity": popularity
            },
            "username": window.localStorage.getItem("email"),
            "playlistId": {
                "creatorUsername": window.localStorage.getItem("creatorUsername"),
                "playlistName": window.localStorage.getItem("playlistName")
            }
        }, {headers: {'Content-Type': 'application/json'}})
    }

    function removeFromSelectedSongs(songId) {
        //TODO : call an axios message to remove from database
        if (selectedSongs.includes(songId)) {
            setSelectedSongs(selectedSongs.filter((id) => id !== songId));
        }
        //  let response = axios.delete("http://localhost:8080/deleteFromPlaylist",{"song" : {"songId" : "", "authorId" : "","popularity": 3},"playlistId" : {"creatorUsername" : "", "playListName" : ""}  , "username" : "user1"})
        let response = axios.post("http://localhost:8080/deleteFromPlaylist",{"username" : window.localStorage.getItem("email"), "playlistId" : {"creatorUsername" : window.localStorage.getItem("creatorUsername"), "playlistName" : window.localStorage.getItem("playlistName")}, "songId" : songId})

    }

    const renderAuthorTracks = () => {
        return authorTrack.map((track) => (
            <div key={track.id} className="track-item">
                <img
                    src={track.album.images[0].url}
                    alt={`${track.name} Album Cover`}
                    className="track-image"
                />
                <div className="track-info">
                    <div className="track-name">{track.name}</div>
                    <div className="track-name"> 📈 {track.popularity}</div>
                    <div className="track-artist">
                    Artist: {track.artists[0].name}
                        {!selectedSongs.includes(track.id) && (
                            <button className="heart-button"
                                    onClick={() => addToSelectedSongs(track.id, track.artists[0].id, track.popularity)}>
                                ❤
                            </button>
                        )}
                        {selectedSongs.includes(track.id) && (
                            <button className="heart-button"
                                    onClick={() => removeFromSelectedSongs(track.id, track.artists[0].id, track.popularity)}>
                                ❤️
                            </button>
                        )}
                    </div>
                </div>
            </div>
        ));
    };

    const renderAuthorTracks2 = () => {
        return authorTrack2.map((track) => (
            <div key={track.id} className="track-item">
                <img
                    src={track.album.images[0].url}
                    alt={`${track.name} Album Cover`}
                    className="track-image"
                />
                <div className="track-info">
                    <div className="track-name">{track.name}</div>
                    <div className="track-name"> 📈 {track.popularity}</div>
                    <div className="track-artist">
                        Artist: {track.artists[0].name}
                        {!selectedSongs.includes(track.id) && (
                            <button className="heart-button"
                                    onClick={() => addToSelectedSongs(track.id, track.artists[0].id, track.popularity)}>
                                ❤
                            </button>
                        )}
                        {selectedSongs.includes(track.id) && (
                            <button className="heart-button"
                                    onClick={() => removeFromSelectedSongs(track.id, track.artists[0].id, track.popularity)}>
                                ❤️
                            </button>
                        )}
                    </div>
                </div>
            </div>
        ));
    };


    const getPlaylist = async (e) => {
        e.preventDefault();
        let token = window.localStorage.getItem("token");
        const { data } = await axios.get("https://api.spotify.com/v1/me/playlists", {
            headers: {
                Authorization: "Bearer " + token,
            },
        });
        setPlaylistItems(data.items);
        setPlaylist(true);
        setDisplay(false);
    };

    const getSongsFromArtist = async (e) => {
        e.preventDefault();
        let token = window.localStorage.getItem("token");
        const { data } = await axios.get("https://api.spotify.com/v1/search?type=track&q=" + inputValue + "&limit=50", {
            headers: {
                Authorization: "Bearer " + token,
            }
        });
        console.log(data.tracks.items);
        setAuthorTrack(data.tracks.items);
    };

    const closeAll = async (e) => {
        e.preventDefault();
        setDisplay(true);
        setSelectedPlaylist(null);
        setSelectedArtistInfo(null);
        setPlaylist(false);
        setSelectedPlaylist(null);
        setShowArtistInfo(false);
        setPlaylistItems([]);

    }

    const getTrackesBasedOnGenreAndAll = async (e) => {
        e.preventDefault();
        let token = window.localStorage.getItem("token");
        let genres = selectedGenres.join(",");
        let min = rangeValues.min;
        let max = rangeValues.max;
        const { data } = await axios.get("https://api.spotify.com/v1/recommendations?limit=50&market=US&seed_genres=" + genres + "&min_popularity=" + min + "&max_popularity=" + max, {
            headers: {
                Authorization: "Bearer " + token,
            }
        });
        console.log(data.tracks);
        setAuthorTrack2(data.tracks);
    };


    const createCustomPlayist = async (e) => {
        e.preventDefault();
        let response = axios.post("http://localhost:8080/createCustomPlaylist", {
            "token": window.localStorage.getItem("token"),
            "playlistId": {
                "creatorUsername": window.localStorage.getItem("creatorUsername"),
                "playlistName": window.localStorage.getItem("playlistName")
            },
            "id" : window.localStorage.getItem("username_id")
        }, {
            headers: {
                'Content-Type': 'application/json'
            }
        }).then((response) => {setHref(response.data); setPlaylistCreated(true)});
    };

    return (
        <div className="playlist-container">
            <div>{renderPlaylist()}</div>
            {selectedPlaylist !== null && (
                <button className="get-playlist-button-close" onClick={closeAll}>
                    Close playlist & playlist items
                </button>
            )}
            {selectedPlaylist !== null && (
                <div>


                    <div>{renderTracks()}</div>
                </div>
            )}
            {displayPlay && (
                <button style={{marginLeft: '70px', marginRight: '70px', marginTop: '10px', marginBottom: '0px'}} onClick={getPlaylist}>
                    Get your playlists from Spotify
                </button>
            )}
          <hr className="double-line" />
            <div>   <div>
                <label>Select genres:</label>
                <div>
                    {genres.map((genre, index) => (
                        <div key={index}>
                            <input
                                type="checkbox"
                                id={genre}
                                value={genre}
                                checked={selectedGenres.includes(genre)}
                                onChange={() => handleCheckboxChange(genre)}
                            />
                            <label htmlFor={genre}>{genre}</label>
                        </div>
                    ))}
                </div>
                <p>Selected genres: {selectedGenres.join(', ')}</p>
            </div>
                <div>
                    <label htmlFor="min-input">Min:</label>
                    <input
                        type="number"
                        id="min-input"
                        min="1"
                        max="100"
                        value={rangeValues.min}
                        onChange={(e) => handleInputChange(e, 'min')}
                    />

                    <label htmlFor="max-input">Max:</label>
                    <input
                        type="number"
                        id="max-input"
                        min="1"
                        max="100"
                        value={rangeValues.max}
                        onChange={(e) => handleInputChange(e, 'max')}
                    />

                    <p>
                        Selected range: {rangeValues.min} - {rangeValues.max}
                    </p>
                </div>
                <button onClick={getTrackesBasedOnGenreAndAll}></button>
                <div>{renderAuthorTracks2()}</div>
            </div>
            <hr className="double-line" />
            {displaySearch && (
                <div className="searchBox">
                    <label htmlFor="textInput">Search artists on Spotify by name</label>
                    <input style={{marginLeft: '70px', marginRight: '70px', marginTop: '10px', marginBottom: '0px'}}
                        type="text"
                        id="textInput"
                        value={inputValue}
                        onChange={handleChange}
                        placeholder="Type something..."
                    />

                    <button style={{marginLeft: '70px', marginRight: '70px', marginTop: '10px', marginBottom: '0px'}}
                        disabled={inputValue.length === 0}
                        onClick={getSongsFromArtist}>
                            Search
                    </button>
                    <button style={{marginLeft: '70px', marginRight: '70px', marginTop: '10px', marginBottom: '0px'}}
                        onClick={() => {
                        setAuthorTrack(null);}}
                        >
                            X
                    </button>
                    {authorTrack != null  && (
                        <div >
                            <h3>Found these tracks</h3>
                            <div>{renderAuthorTracks()}</div>
                        </div>
                    )}
                        {window.localStorage.getItem("creatorUsername") === window.localStorage.getItem("email") &&
                <div><button style={{marginLeft: '70px', marginRight: '70px', marginTop: '10px', marginBottom: '0px'}} onClick={createCustomPlayist}>CREATE FINAL PLAYLIST FOR : {window.localStorage.getItem("playlistName")}</button></div>}
                    {playlistCreated &&      <a href={href} target="_blank" rel="noopener noreferrer">CREATED PLAYLIST</a>}
                </div>
            )}

        </div>
    );
}

export default Playlist;
