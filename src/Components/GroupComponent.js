import Playlist from "./Playlist";
import {useState} from "react";


function GroupComponent() {
// TODO : input text to see if a group exists you want to join, or a button to create a group
    // treba napraviti kad se korisnik ulogira da se ili pridruzi grupi ili napravi novu grupu, sve to se preko axiosa salje u backedn i onda se tamo salje u bazu podataka,
    // nakon sto uspjesno napravi grupu ili pri druzi se grupi otvara se opcija da vidi svoje playlise i tarckove

    const [seePlaylist, setSeePlaylist] = useState(false)
    const  [seeCreateGroup, setSeeCreateGroup] = useState(false)
    const [seeEnter, setSeeEnter] = useState(true)
    function enterAGroup() {
        //TODO : axios call to backend to see if group exists, if a group exists setSeePlalyist to true
        setSeePlaylist(false)
        setSeeCreateGroup(true)
        setSeeEnter(false)

    }

    function createAGroup() {
        // TODO : axios call to backend to create a group, if a group is created setSeePlalyist to true, send a request with the name of the group and the email of the user
        //get email from session stroage
        let emailOfLoginUser = window.localStorage.getItem("email")
        setSeeCreateGroup(false)
        setSeePlaylist(true)

    }

    return (
        <div >
            {seeEnter && 
            <div className="group-container"> 
                Enter name of group to access group
                <hr className="double-line" />
                <input type="text" placeholder="Enter group name"/>
                <button onClick={enterAGroup}>Enter a group</button>
            </div>}
            <div>
                {seePlaylist && <Playlist/>}
            </div>
            {seeCreateGroup && 
            <div className="group-container">
                Enter name of group to create group
                <hr className="double-line" />
                <input type="text" placeholder="Enter group name you want to create"/>
                <button onClick={createAGroup}>Create a group</button>
                </div>
            }
        </div>

)
;
}

export default GroupComponent;