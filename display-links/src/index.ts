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
                            // TODO
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
