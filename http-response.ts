import { request } from "https";

export enum Status {
  "SUCCESS" = "SUCCESS",
  "FAILED" = "FAILED",
}

await new Promise<void>((resolve, reject) => {
    const options = {
      method: "PUT",
      headers: { "content-type": "" },
    };
    request(props.event.ResponseURL, options)
      .on("error", (err) => {
        reject(err);
      })
      .end(JSON.stringify(response), "utf8", resolve);
  });
