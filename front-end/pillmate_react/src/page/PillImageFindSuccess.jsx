import React, {useEffect, useState} from 'react';
import PillHeader from '../component/PillHeader';
import {pillAllInformation} from "../utils/PillAllInformation";
import PillDetails from "../component/PillDetails";
import {useNavigate} from "react-router-dom";

const pillAllList = pillAllInformation();
const PillImageFindSuccess = () => {
    const [predictionResult, setPredictionResult] = useState('');
    const [predictionList, setPredictionList] = useState([])
    const navigate = useNavigate()

    useEffect(() => {
        const fetchData = async () => {
            try {
                // Make a request to fetch the content of output.txt
                const response = await fetch('/output.txt');

                // Check if the request was successful (status code 200)
                if (response.ok) {
                    // Read the response body as text
                    const text = await response.text();
                    // Split the text into lines and get the first line
                    setPredictionResult(text);
                    setPredictionList(pillAllList.filter((it) => it.dl_name.trim() === text.trim()))
                } else {
                    console.error('Failed to fetch file:', response.status);
                }
            } catch (error) {
                console.error('Error fetching file:', error);
            }
        };
        fetchData()
    }, []);
    const goProfile = () => {
        navigate("/PillProfile")
    }
    return (
        <div>
            <PillHeader onClick={goProfile}/>
            {predictionList.map((it) =><PillDetails key={it.id} {...it} successList = {it}></PillDetails>)}
        </div>
    );
};

export default React.memo(PillImageFindSuccess);
