import logo from './logo.svg';
import './App.css'

import {useEffect, useRef, useState} from "react";
import axios from "axios";
import Playlist from "./Components/Playlist";
import {addDoc, collection} from "@firebase/firestore"
import {db} from "./firebase";
import handleSubmit from "./handlers/handleSubmit";
import handleMsg from "./handlers/handleMsg";
import {doc, getDoc} from "firebase/firestore";
import {  getDocs } from "firebase/firestore";
import GroupComponent from "./Components/GroupComponent";


function App() {

    // const CLIENT_ID = '59097828c7c14767b30fdc16ede83d49'
    const CLIENT_ID = '59097828c7c14767b30fdc16ede83d49'
    const REDIRET_URI = 'http://localhost:3000/api/auth/callback/spotify'
    const AUTH_ENDPOINT = 'https://accounts.spotify.com/authorize'
    const RESPONSE_TYPE = 'token'
    const SCOPES = 'user-read-private user-read-email playlist-modify-public'; // Include 'user-read-email' scope
    const [token, setToken] = useState("")


    useEffect(() => {
        const hash = window.location.hash
        let token = window.localStorage.getItem("token")
        if (!token && hash) {
            token = hash.substring(1).split("&").find(el => el.startsWith("access_token")).split("=")[1]
            window.location.hash = ""
            window.localStorage.setItem("token", token)
        }
        setToken(token)
        if (token) {
            axios.get("https://api.spotify.com/v1/me", {
                headers: {
                    Authorization: 'Bearer ' + token
                }
            })
                .then(response => {
                    console.log(response.data)
                    if (response.data.email) {
                        const email = response.data.email;
                        let id = response.data.id;
                        window.localStorage.setItem("email", email)
                        window.localStorage.setItem("username_id", id)
                        let username = window.localStorage.getItem("email")
                        axios.post("http://localhost:8080/createUser",{
                            username
                        }, {headers: {
                                'Content-Type': 'application/json'
                            }});
                    } else {
                        console.log("Email not available");
                    }
                })
                .catch(error => {
                    console.error("Error fetching user information:", error);
                });
        }
    }, [])

    const retrieveUserData = async (email) => {
        const loginRef = doc(db, "login", email);

        try {
            const docSnap = await getDoc(loginRef);
            if (docSnap.exists()) {
                const userData = docSnap.data();
                console.log("User Data:", userData);
            } else {
                console.log("No such document!");
            }
        } catch (e) {
            console.error("Error getting document:", e);
        }
    };
    const logOut = () => {
        setToken("")
        window.localStorage.removeItem("token")

    }



    return (
        <div className="App">
            <header className="App-header">
                    {token && (
                        <GroupComponent></GroupComponent>
                    )}
                <div>
                        {token ? (
                            <div className="user-info-box">
                                <label>You are currently logged in Spotify</label>
                                <button onClick={logOut}>Log out</button>
                                </div>            
                        ) : (
                            <div className='login-form-container'>
                                <form className="login-form">
                                    Please login to Spotify
                                    <a href={`${AUTH_ENDPOINT}?client_id=${CLIENT_ID}&redirect_uri=${REDIRET_URI}&response_type=${RESPONSE_TYPE}&scope=${encodeURIComponent(SCOPES)}`}>Login</a>
                                </form>
                            </div>
                        )}
                </div>
            </header>
        </div>
    );

}

export default App;
