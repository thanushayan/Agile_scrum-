const API_BASE_URL = "https://api.videosdk.live";
const API_AUTH_URL = process.env.REACT_APP_AUTH_URL;

export const authToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhcGlrZXkiOiJkMGFmOTIzYy1kYWMwLTQwMDYtOTk2Mi00MTllNTI5MGY3YjkiLCJwZXJtaXNzaW9ucyI6WyJhbGxvd19qb2luIl0sImlhdCI6MTY5ODgxNTM2MywiZXhwIjoxNzE0MzY3MzYzfQ.vXPj2ZcEVrCCYPdvc2QxUWXjtg5d50VcJdS8ktG4ALw";

export const getToken = async () => {
  if (authToken && API_AUTH_URL) {
    console.error(
      "Error: Provide only ONE PARAMETER - either Token or Auth API"
    );
  } else if (authToken) {
    return authToken;
  } else if (API_AUTH_URL) {
    const res = await fetch(`${API_AUTH_URL}/get-token`, {
      method: "GET",
      headers: {
        authorization: `${authToken}`, // <-- Potential issue: 'authToken' variable is not defined here
        "Content-Type": "application/json",
      },
    });
    const { token } = await res.json();
    return token;
  } else {
    console.error("Error: ", Error("Please add a token or Auth Server URL"));
  }
};



export const createMeeting = async () => {
  const res = await fetch(`https://api.videosdk.live/v1/meetings`, {
    method: "POST",
    headers: {
      authorization: `${authToken}`,
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ region: "sg001" }),
  });

  const { meetingId } = await res.json();
  return meetingId;
};


// export const authToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhcGlrZXkiOiJkMGFmOTIzYy1kYWMwLTQwMDYtOTk2Mi00MTllNTI5MGY3YjkiLCJwZXJtaXNzaW9ucyI6WyJhbGxvd19qb2luIl0sImlhdCI6MTY5ODgxNTM2MywiZXhwIjoxNzE0MzY3MzYzfQ.vXPj2ZcEVrCCYPdvc2QxUWXjtg5d50VcJdS8ktG4ALw";

// // API call to create meeting
// export const createMeeting = async () => {
//   const res = await fetch(`https://api.videosdk.live/v1/meetings`, {
//     method: "POST",
//     headers: {
//       authorization: `${authToken}`,
//       "Content-Type": "application/json",
//     },
//     body: JSON.stringify({ region: "sg001" }),
//   });

//   const { meetingId } = await res.json();
//   return meetingId;
// };

export const validateMeeting = async ({ roomId, token }) => {
  const url = `${API_BASE_URL}/v2/rooms/validate/${roomId}`;

  const options = {
    method: "GET",
    headers: { Authorization: token, "Content-Type": "application/json" },
  };

  const result = await fetch(url, options)
    .then((response) => response.json()) //result will have meeting id
    .catch((error) => console.error("error", error));

  return result ? result.roomId === roomId : false;
};
