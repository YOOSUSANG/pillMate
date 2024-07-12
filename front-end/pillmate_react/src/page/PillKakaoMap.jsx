import PillHeader from "../component/PillHeader";
import {Map, MapMarker} from "react-kakao-maps-sdk";
import React, {useEffect, useState} from "react";
import PillNav from "../component/PillNav";
import PillAllNav from "../component/PillAllNav";
import {useNavigate} from "react-router-dom";
import useAuthorization from "../validaiton/useauthorization";


const PillKakaoMap = () => {
    useAuthorization()
    const [markers, setMarkers] = useState([]);
    const [map, setMap] = useState();
    const [searchText, setSearchText] = useState("");
    const [userLocation, setUserLocation] = useState({lat: 37.566826, lng: 126.9786567});
    const [activeMarker, setActiveMarker] = useState(null);
    const navigate = useNavigate()

    const goProfile = () => {
        navigate("/PillProfile")
    }
    // This function is called when a marker is clicked
    const handleMarkerClick = (marker) => {
        // Check if the clicked marker is already active, if so, deactivate it
        if (activeMarker === marker) {
            setActiveMarker(null);
        } else {
            // Set the clicked marker as active to show its name above it
            setActiveMarker(marker);
        }
    };

    useEffect(() => {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition((position) => {
                setUserLocation({
                    lat: position.coords.latitude,
                    lng: position.coords.longitude,
                });
            }, (error) => {
                console.error("Error Code = " + error.code + " - " + error.message);
            });
        }
    }, []);

    useEffect(() => {
        if (!map || searchText.trim() === "") return;

        // Function to handle search
        const handleSearch = () => {
            const ps = new window.kakao.maps.services.Places();
            const bounds = map.getBounds();
            ps.keywordSearch(searchText, (data, status, _pagination) => {
                if (status === window.kakao.maps.services.Status.OK) {
                    const newMarkers = data
                        .filter(item => bounds.contain(new window.kakao.maps.LatLng(item.y, item.x)))
                        .map(item => ({
                            position: {lat: item.y, lng: item.x},
                            content: item.place_name,
                            phone: item.phone
                        }));
                    setMarkers(newMarkers);
                }
            }, {bounds: bounds});
        };

        // Debounce the search to avoid excessive API calls
        const timeoutId = setTimeout(() => {
            handleSearch();
        }, 500); // Adjust the debounce time as needed

        return () => clearTimeout(timeoutId); // Clear the timeout if the component unmounts
    }, [map, searchText]); // Trigger the effect when searchText changes
    return (
        <div>
            <PillAllNav pillNavState={"pillMap"} onClick={goProfile}></PillAllNav>
            <div className="search-container">
                <input className='searchPill'
                       type="text"
                       placeholder="검색어를 입력하세요"
                       value={searchText}
                       onChange={(e) => setSearchText(e.target.value)}
                    //onKeyPress={(e) => { if (e.key === 'Enter') setSearchText(e.target.value) }}
                />
            </div>
            <div className='pill_map_container'>
                <Map
                    center={userLocation}
                    level={3}
                    onCreate={setMap}
                    className="pill_map">
                    {markers.map((marker, index) => (
                        <MapMarker
                            key={`marker-${index}`} // Changed to index for uniqueness
                            position={marker.position}
                            onClick={() => handleMarkerClick(marker)}
                        >
                            {/* Show marker content when it is active */}
                            {activeMarker === marker && (
                                <div style={{
                                    padding: '5px',
                                    color: '#000', // Adjust as needed for exact positioning
                                    backgroundColor: 'white',
                                    borderRadius: '5px',
                                    fontSize: '12px',
                                    whiteSpace: 'nowrap'
                                }}>
                                    {marker.content}
                                </div>
                            )}
                        </MapMarker>
                    ))}
                </Map>
            </div>
            <div style={{textAlign: "center", marginTop: "20px"}}>
                <span className='nearInform'></span>
                <ul>
                    {markers.map((marker, index) => (
                        <li key={index} className="marker-details">
                        <span className={`content ${activeMarker === marker ? 'active-content' : ''}`}>
                            {marker.content}
                        </span>
                            <span className={`phone-number ${!marker.phone && 'no-phone'}`}>
                            전화번호: {marker.phone || '정보 없음'}
                        </span>
                        </li>
                    ))}
                </ul>
            </div>
        </div>
    );
};

export default PillKakaoMap;