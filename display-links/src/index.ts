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
        return Response.redirect("https://example.com", 303); // TODO
    },
} satisfies ExportedHandler<Env>;
