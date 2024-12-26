import Image from "next/image";

export default function SiteHeader() {
    return (
        <header className="sticky top-0 w-full h-16 flex flex-row px-4 space-x-4 justify-between items-center border">
            <div className="flex flex-row space-x-4 items-center">
                <Image
                    src="/sheng-ji-light.svg"
                    width={32}
                    height={32}
                    alt="升级"
                    unoptimized
                    className="h-8 w-8"
                />
                <div className="h-6 w-[1px] bg-slate-500" />
                <Image
                    src="/sheng-ji-guide.svg"
                    width={32}
                    height={32}
                    alt="升级 Guide"
                    unoptimized
                    className="h-8 w-8"
                />
                <h1 className="font-semibold text-xl">升级 Guide</h1>
            </div>
            <p>theme switcher</p>
        </header>
    );
}
