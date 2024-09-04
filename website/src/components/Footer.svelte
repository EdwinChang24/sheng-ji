<script lang="ts">
    import shengJi from "../assets/sheng-ji.svg";
    import edwinChang from "../assets/edwin-chang.svg";
    import github from "../assets/github.svg";

    function scrollToTop() {
        document.body.scrollIntoView();
    }
</script>

<div
    class="grid grid-cols-2 lg:grid-cols-[repeat(3,minmax(0,1fr))_minmax(0,2fr)_repeat(3,minmax(0,1fr))] gap-8 justify-items-center items-center p-12 bg-slate-950"
>
    <a
        href="https://github.com/EdwinChang24/sheng-ji"
        class="col-span-full lg:col-span-1 lg:col-start-4 flex items-center space-x-2"
    >
        <img class="h-6" src={github.src} alt="GitHub" />
        <p class="font-medium text-sm">EdwinChang24/sheng-ji</p>
    </a>
    <a
        href="/"
        class="col-span-1 justify-self-end lg:justify-self-start lg:col-start-1 lg:row-start-1"
    >
        <img class="h-24" src={shengJi.src} alt="升级" />
    </a>
    <a
        href="https://edwinchang.dev"
        class="col-span-1 justify-self-start lg:justify-self-end lg:col-start-7"
    >
        <img class="h-24" src={edwinChang.src} alt="Edwin Chang" />
    </a>
    <button
        class="col-span-full lg:row-start-1 lg:col-span-1 lg:col-start-6 text-sm text-slate-400 text-center"
        on:click={scrollToTop}>Back to top</button
    >
    <a
        href="/display/downloads"
        class="col-span-full lg:row-start-1 lg:col-span-1 lg:col-start-5 text-sm text-slate-400 text-center"
    >
        Download
    </a>
    <a
        href="/privacy"
        class="col-span-full lg:row-start-1 lg:col-span-1 lg:col-start-3 text-sm text-slate-400 text-center"
    >
        Privacy
    </a>
    <a
        href="https://edwinchang.dev"
        class="col-span-full lg:row-start-1 lg:col-span-1 lg:col-start-2 text-sm text-slate-400 text-center"
    >
        © {new Date().getFullYear()} Edwin Chang
    </a>
</div>
