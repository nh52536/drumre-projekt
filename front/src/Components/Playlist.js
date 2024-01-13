// Playlist.js

import React, {useEffect, useRef, useState} from "react";
import axios from "axios";
import "./Playlist.css";
import {doc, setDoc, deleteDoc} from "firebase/firestore";
import {db} from "../firebase";

function Playlist({username}) {
    const [playlistItems, setPlaylistItems] = useState([]);
    const [playlist, setPlaylist] = useState(false);
    const [playlistTracks, setPlaylistTracks] = useState([]);
    const [selectedPlaylist, setSelectedPlaylist] = useState(null);
    const [displayPlay, setDisplay] = useState(true);
    const [selectedArtistInfo, setSelectedArtistInfo] = useState(null);
    const [showArtistInfo, setShowArtistInfo] = useState(false);
    const [isLiked, setIsLiked] = useState(true);
    const [trackNameLiked, setTrackNameLiked] = useState([]);
    const [selectedSongs, setSelectedSongs] = useState([]);
    const [authorTrack, setAuthorTrack] = useState([]);
    const [displaySearch, setDisplaySearch] = useState(true);
    const [inputValue, setInputValue] = useState('');
    const [displayX, setDisplayX] = useState(true);
    useEffect(() => {
        // TODO: call an axios message to get all the songs from the database for the user logged in
        let response = axios.get("http://localhost:8080/likedSongs",{"username": "","playlistId" : {"creatorUsername" : "", "playListName" : ""}})
        console.log(response)
        // TODO add response songs to selectedSongs
        console.log(selectedSongs)
    }, [selectedSongs]);
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

    const getTracks = async (playlistId) => {
        let token = window.localStorage.getItem("token");
        const {data} = await axios.get(
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
                    <div className="track-name">{track.track.name}</div>
                    <div className="track-artist">
                        Artist: {track.track.artists[0].name}
                        {!selectedSongs.includes(track.track.id) && (
                            <button className="heart-button" onClick={() => addToSelectedSongs(track.track.id)}>
                                ❤
                            </button>
                        )}
                        {selectedSongs.includes(track.track.id) && (
                            <button className="heart-button" onClick={() => removeFromSelectedSongs(track.track.id)}>
                                ❤️
                            </button>
                        )}
                    </div>
                </div>
            </div>
        ));
    };

    function addToSelectedSongs(songId) {
        //TODO : call an axios message to save to database
        if (!selectedSongs.includes(songId)) {
            setSelectedSongs([...selectedSongs, songId]);
        }

        let response = axios.post("http://localhost:8080/addToPlaylist",{"song" : {"songId" : "", "authorId" : "","popularity": 3},"username" : "user1", "playlistId" : {"creatorUsername" : "", "playListName" : ""}})
    }

    function removeFromSelectedSongs(songId) {
        //TODO : call an axios message to remove from database
        if (selectedSongs.includes(songId)) {
            setSelectedSongs(selectedSongs.filter((id) => id !== songId));
        }
        let response = axios.delete("http://localhost:8080/deleteFromPlaylist",{"username" : "user1", "playlistId" : {"creatorUsername" : "", "playListName" : ""}, "songId" : ""})

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
                    <div className="track-artist">{track.artists[0].name}
                        {!selectedSongs.includes(track.id) && (
                            <button className="heart-button" onClick={() => addToSelectedSongs(track.id)}>
                                ❤
                            </button>
                        )}
                        {selectedSongs.includes(track.id) && (
                            <button className="heart-button" onClick={() => removeFromSelectedSongs(track.id)}>
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
        const {data} = await axios.get("https://api.spotify.com/v1/me/playlists", {
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
        const {data} = await axios.get("https://api.spotify.com/v1/search?type=track&q=" + inputValue + "&limit=50", {
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

    return (
        <div className="playlist-container">
            {!displayPlay && (
                <button className="get-playlist-button-close" onClick={closeAll}>
                    X
                </button>
            )}
            <div>{renderPlaylist()}</div>
            {selectedPlaylist !== null && (
                <div>
                    <h3>Tracks of {selectedPlaylist}</h3>
                    <div>{renderTracks()}</div>
                </div>
            )}
            {displayPlay && (
                <button className="get-playlist-button" onClick={getPlaylist}>
                    GET YOUR PLAYLIST FROM SPOTIFY
                </button>
            )}

            {displaySearch && (

                <div>

                    <label htmlFor="textInput">Enter Text:</label>
                    <input
                        type="text"
                        id="textInput"
                        value={inputValue}
                        onChange={handleChange}
                        placeholder="Type something..."
                    />
                    <button disabled={inputValue.length == 0} onClick={getSongsFromArtist}>SEARCH</button>
                 
                    {authorTrack !== null && (
                        <div>
                            <h3>Tracks of {selectedPlaylist}</h3>
                            <div>{renderAuthorTracks()}</div>
                        </div>
                    )} </div>)}
        </div>
    );
}

export default Playlist;
