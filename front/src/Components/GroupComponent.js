import Playlist from "./Playlist";
import {useState} from "react";
import axios from "axios";


function GroupComponent() {
// TODO : input text to see if a group exists you want to join, or a button to create a group
    // treba napraviti kad se korisnik ulogira da se ili pridruzi grupi ili napravi novu grupu, sve to se preko axiosa salje u backedn i onda se tamo salje u bazu podataka,
    // nakon sto uspjesno napravi grupu ili pri druzi se grupi otvara se opcija da vidi svoje playlise i tarckove

    const [seePlaylist, setSeePlaylist] = useState(false)
    const  [seeCreateGroup, setSeeCreateGroup] = useState(false)
    const [playListName, setPlayListName] = useState("")
    const [creator,setCreator] = useState("")
    const [seeEnter, setSeeEnter] = useState(true)
    const [groupName, setGroupName] = useState("")
    const [warning, setWarning] = useState(false)
    const [noPlaylist, setNoPlaylist] = useState(false)
    async function enterAGroup() {
        //TODO : axios call to backend to see if group exists, if a group exists setSeePlalyist to true
        let response = await axios.post("http://localhost:8080/join", {
            "username": window.localStorage.getItem("email"),
            "playlistId": {"creatorUsername": creator, "playlistName": playListName}
        }, {
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (response.data == true) {
            setSeePlaylist(true)
            window.localStorage.setItem("creatorUsername", creator)
            window.localStorage.setItem("playlistName", playListName)
            setNoPlaylist(false)
            setSeeEnter(false)
            setSeePlaylist(true)
        } else {
            setNoPlaylist(true)
        }

    }

    async function createAGroup() {
        // TODO : axios call to backend to create a group, if a group is created setSeePlalyist to true, send a request with the name of the group and the email of the user
        let response = await axios.post("http://localhost:8080/createPlaylist",{playlistName:groupName,username:window.localStorage.getItem("email")}, {
            headers: {
                'Content-Type': 'application/json'
            }});
       if(response.data == true) {
           setWarning(false)
           console.log(creator + playListName)
           window.localStorage.setItem("creatorUsername", window.localStorage.getItem("email"))
           window.localStorage.setItem("playlistName", groupName)
           setSeePlaylist(true)
           setSeeCreateGroup(false)
       }else if (response.data == false) {
           setSeePlaylist(false)
           setWarning(true)
       }

    }
    const handleInputChange = (event) => {
        setPlayListName(event.target.value);
    };

    const handleInputChange2 = (event) => {
        setCreator(event.target.value);
    };

    const handleInputChange3 = (event) => {
        setGroupName(event.target.value);
    };



    return (
        <div>
            {seeEnter && <div> <input type="text"  value={playListName}
                                      onChange={handleInputChange} placeholder="Enter group name"/>
                <div><input type="text" value={creator}
                            onChange={handleInputChange2} placeholder="Enter a creator"/></div>
                <button onClick={enterAGroup}>Enter a group</button>
            <button onClick={() => {setSeeCreateGroup(true); setSeeEnter(false)}}>I WANT TO CREATE A PLAYLIST </button></div>}
            {seePlaylist && <Playlist/>}
            {noPlaylist && <div>NO SUCH PLAYLIST EXISTS</div>}

            {seeCreateGroup && <div><input type="text"  value={groupName}
                                           onChange={handleInputChange3} placeholder="Enter group name you want to create"/><button onClick={createAGroup}>CREATE A GROUP</button>
                {warning && <div>A PLAYLIST WITH THAT NAME ALREADY EXISTS </div>}</div>

            }
        </div>
)
;
}

export default GroupComponent;