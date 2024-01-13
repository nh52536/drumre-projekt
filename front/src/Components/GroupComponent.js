import Playlist from "./Playlist";
import {useState} from "react";
import axios from "axios";


function GroupComponent() {
// TODO : input text to see if a group exists you want to join, or a button to create a group
    // treba napraviti kad se korisnik ulogira da se ili pridruzi grupi ili napravi novu grupu, sve to se preko axiosa salje u backedn i onda se tamo salje u bazu podataka,
    // nakon sto uspjesno napravi grupu ili pri druzi se grupi otvara se opcija da vidi svoje playlise i tarckove

    const [seePlaylist, setSeePlaylist] = useState(false)
    const  [seeCreateGroup, setSeeCreateGroup] = useState(false)
    const [seeEnter, setSeeEnter] = useState(true)
    function enterAGroup() {
        //TODO : axios call to backend to see if group exists, if a group exists setSeePlalyist to true
        let response = axios.post("http://localhost:8080/join",{"playlistId" : {"creatorUsername" : "", "playListName" : ""}  , "username" : "user1"});
        if(response.body == true) {
            setSeePlaylist(true)
            //setSeeCreateGroup(false)

        }else {
            setSeePlaylist(false)
           // setSeeCreateGroup(true)
        }
        setSeeEnter(false)

    }

    function createAGroup() {
        // TODO : axios call to backend to create a group, if a group is created setSeePlalyist to true, send a request with the name of the group and the email of the user
        //get email from session stroage

        var response = axios.post("http://localhost:8080/createPlaylist",{"request" : {"playlistName":"playlistNAME","user" : {"username":window.localStorage.getItem("email")}}})
       if(response.body == true) {
              setSeePlaylist(true)
       }else if (response.body == false) {
           setSeePlaylist(false)
       }

        let emailOfLoginUser = window.localStorage.getItem("email")
        setSeeCreateGroup(false)
        setSeePlaylist(true)
    }

    return (
        <div>
            {seeEnter && <div> <input type="text" placeholder="Enter group name"/>
                <div><input type="text" placeholder="Enter a creator"/></div>
                <button onClick={enterAGroup}>Enter a group</button></div>}
            //TODO : input parameters for creating a group
            {seePlaylist && <Playlist/>}
            {seeCreateGroup && <div><input type="text"  placeholder="Enter group name you want to create"/><button onClick={createAGroup}>CREATE A GROUP</button></div>
            }
        </div>
)
;
}

export default GroupComponent;