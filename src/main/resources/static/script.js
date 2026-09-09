const startButton =
    document.getElementById("startButton");

const stopButton =
    document.getElementById("stopButton");

const statusText =
    document.getElementById("status");

const transcription =
    document.getElementById("transcription");


let mediaRecorder = null;

let audioChunks = [];

let microphoneStream = null;


startButton.addEventListener(
    "click",
    startRecording
);

stopButton.addEventListener(
    "click",
    stopRecording
);


async function startRecording() {

    try {

        microphoneStream =
            await navigator.mediaDevices.getUserMedia({
                audio: true
            });

        audioChunks = [];

        mediaRecorder =
            new MediaRecorder(microphoneStream);

        mediaRecorder.addEventListener(
            "dataavailable",
            event => {

                if (event.data.size > 0) {
                    audioChunks.push(event.data);
                }

            }
        );

        mediaRecorder.addEventListener(
            "stop",
            handleRecordingStopped
        );

        mediaRecorder.start();

        startButton.disabled = true;
        stopButton.disabled = false;

        statusText.textContent =
            "Recording...";

        transcription.textContent =
            "Recording in progress.";

    }
    catch (error) {

        console.error(
            "Could not access microphone:",
            error
        );

        statusText.textContent =
            "Microphone access failed.";
    }
}


function stopRecording() {

    if (
        mediaRecorder !== null &&
        mediaRecorder.state === "recording"
    ) {

        mediaRecorder.stop();

        stopButton.disabled = true;

        statusText.textContent =
            "Processing recording...";
    }
}


async function handleRecordingStopped() {

    const audioBlob =
        new Blob(
            audioChunks,
            {
                type: mediaRecorder.mimeType
            }
        );

    if (microphoneStream !== null) {

        microphoneStream
            .getTracks()
            .forEach(track => track.stop());
    }

    transcription.textContent =
        `Recorded ${audioBlob.size} bytes of audio.`;

    statusText.textContent =
        "Recording complete.";

    startButton.disabled = false;
}