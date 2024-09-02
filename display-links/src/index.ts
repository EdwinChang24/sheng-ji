export default {
    async fetch(request, _env, _ctx): Promise<Response> {
        const url = new URL(request.url);
        if (url.pathname === "/.well-known/assetlinks.json") {
            return Response.json([
                {
                    relation: ["delegate_permission/common.handle_all_urls"],
                    target: {
                        namespace: "android_app",
                        package_name: "dev.edwinchang.shengjidisplay",
                        sha256_cert_fingerprints: [
                            "19:34:28:93:D6:50:6A:7B:0E:90:07:CD:79:FC:04:F8:18:4D:EB:F8:EA:89:12:66:1D:22:D5:39:38:03:AA:9F",
                        ],
                    },
                },
            ]);
        }
        if (url.pathname === "/display") {
            return Response.redirect("https://shengji.edwinchang.dev/display", 303);
        }
        return Response.redirect("https://shengji.edwinchang.dev/downloads/display", 303);
    },
} satisfies ExportedHandler<Env>;
