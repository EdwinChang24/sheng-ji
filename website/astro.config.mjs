import { defineConfig } from "astro/config";
import svelte from "@astrojs/svelte";

import tailwind from "@astrojs/tailwind";

// https://astro.build/config
export default defineConfig({
    build: {
        format: "file",
    },
    integrations: [svelte(), tailwind()],
    redirects: {
        "/downloads": "/downloads/display",
    },
});
