<script lang="ts">
    import shengJi from "../assets/sheng-ji.svg";
    import edwinChang from "../assets/edwin-chang.svg";
    import github from "../assets/github.svg";

    function scrollToTop() {
        document.body.scrollIntoView();
    }
</script>

<div
    class="grid grid-cols-2 lg:grid-cols-5 gap-12 justify-items-center items-center p-12 bg-slate-950"
>
    <a
        href="https://github.com/EdwinChang24/sheng-ji-display"
        class="col-span-full lg:col-span-1 lg:col-start-3 flex items-center space-x-2"
    >
        <img class="h-6" src={github.src} alt="GitHub" />
        <p class="font-medium text-sm">EdwinChang24/sheng-ji-display</p>
    </a>
    <a href="/" class="col-span-1 justify-self-end lg:col-start-1 lg:row-start-1">
        <img class="h-24" src={shengJi.src} alt="升级" />
    </a>
    <a href="https://edwinchang.dev" class="col-span-1 justify-self-start lg:col-start-5">
        <img class="h-24" src={edwinChang.src} alt="Edwin Chang" />
    </a>
    <button
        class="col-span-full lg:row-start-1 lg:col-span-1 lg:col-start-4 text-sm text-slate-400 text-center"
        on:click={scrollToTop}>Back to top</button
    >
    <div
        class="col-span-full lg:row-start-1 lg:col-span-1 lg:col-start-2 text-sm text-slate-400 text-center"
    >
        <p class="inline">© {new Date().getFullYear()}</p>
        <a href="https://edwinchang.dev">Edwin Chang</a>
    </div>
</div>
